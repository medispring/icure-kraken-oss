package org.taktik.icure.couchdb

import java.net.URI
import java.net.URL
import java.nio.ByteBuffer
import java.nio.CharBuffer
import java.nio.charset.StandardCharsets
import java.util.UUID
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.icure.asyncjacksonhttpclient.net.web.WebClient
import io.icure.asyncjacksonhttpclient.parser.EndArray
import io.icure.asyncjacksonhttpclient.parser.StartArray
import io.icure.asyncjacksonhttpclient.parser.StartObject
import io.icure.asyncjacksonhttpclient.parser.split
import io.icure.asyncjacksonhttpclient.parser.toJsonEvents
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.fold
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.taktik.couchdb.ClientImpl
import org.taktik.couchdb.Offset
import org.taktik.couchdb.TotalCount
import org.taktik.couchdb.ViewRow
import org.taktik.couchdb.bulkUpdate
import org.taktik.couchdb.create
import org.taktik.couchdb.entity.ViewQuery
import org.taktik.couchdb.exception.CouchDbConflictException
import org.taktik.couchdb.get
import org.taktik.couchdb.queryView
import org.taktik.couchdb.queryViewIncludeDocs
import org.taktik.couchdb.springramework.webclient.SpringWebfluxWebClient
import org.taktik.couchdb.subscribeForChanges
import org.taktik.couchdb.update
import org.taktik.icure.asyncdao.CodeDAO
import org.taktik.icure.asyncdao.impl.CodeDAOImpl
import org.taktik.icure.entities.base.Code
import org.taktik.icure.properties.CouchDbProperties
import org.taktik.icure.test.ICureTestApplication
import org.taktik.icure.test.TestProperties

@OptIn(ExperimentalCoroutinesApi::class)
@SpringBootTest(
	classes = [ICureTestApplication::class],
	properties = [
		"spring.main.allow-bean-definition-overriding=true",
	],
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("app")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CouchDbClientTest(
	@Autowired testProperties: TestProperties,
	@Autowired couchDbProperties: CouchDbProperties,
	@Autowired codeDAO: CodeDAOImpl,
	) : StringSpec() {
	private val httpClient: WebClient by lazy {
		SpringWebfluxWebClient()
	}

	private val testResponseAsString = URL("https://jsonplaceholder.typicode.com/posts").openStream().use { it.readBytes().toString(StandardCharsets.UTF_8) }

	private val client = ClientImpl(
		httpClient,
		URI("${couchDbProperties.url}/${testProperties.couchdb.databaseName}"),
		testProperties.couchdb.username!!,
		testProperties.couchdb.password!!
	)

	private val client2 = ClientImpl(
		httpClient,
		URI("${couchDbProperties.url}/${UUID.randomUUID()}"),
		testProperties.couchdb.username!!,
		testProperties.couchdb.password!!
	)

	init {
		"subscribe to changes" {
			val testSize = 10
			val deferredChanges = async {
				client.subscribeForChanges<Code>("java_type", { if (it == Code::class.java.canonicalName) Code::class.java else null }).take(testSize).toList()
			}
			// Wait a bit before updating DB
			delay(3000)
			val codes = List(testSize) { Code.from("test", UUID.randomUUID().toString(), "test") }
			val createdCodes = client.bulkUpdate(codes).toList()
			val changes = deferredChanges.await()
			assertEquals(createdCodes.size, changes.size)
			assertEquals(createdCodes.map { it.id }.toSet(), changes.map { it.id }.toSet())
			assertEquals(codes.map { it.code }.toSet(), changes.map { it.doc.code }.toSet())
		}

		"test database exists" {
			Assertions.assertTrue(client.exists())
		}

		"test database does not exist" {
			Assertions.assertFalse(client2.exists())
		}

		"request response by flow" {
			val bytesFlow = httpClient.uri("https://jsonplaceholder.typicode.com/posts").method(io.icure.asyncjacksonhttpclient.net.web.HttpMethod.GET).retrieve().toBytesFlow()

			val bytes = bytesFlow.fold(ByteBuffer.allocate(1000000), { acc, buffer -> acc.put(buffer) })
			bytes.flip()
			val responseAsString = StandardCharsets.UTF_8.decode(bytes).toString()
			assertEquals(testResponseAsString, responseAsString)
		}

		"test request get text" {
			val charBuffers = httpClient.uri("https://jsonplaceholder.typicode.com/posts").method(io.icure.asyncjacksonhttpclient.net.web.HttpMethod.GET).retrieve().toTextFlow()
			val chars = charBuffers.toList().fold(CharBuffer.allocate(1000000), { acc, buffer -> acc.put(buffer) })
			chars.flip()
			assertEquals(testResponseAsString, chars.toString())
		}

		"test request get text and split" {
			val charBuffers = httpClient.uri("https://jsonplaceholder.typicode.com/posts").method(io.icure.asyncjacksonhttpclient.net.web.HttpMethod.GET).retrieve().toTextFlow()
			val split = charBuffers.split('\n')
			val lines = split.map { it.fold(CharBuffer.allocate(100000), { acc, buffer -> acc.put(buffer) }).flip().toString() }.toList()
			assertEquals(testResponseAsString.split("\n"), lines)
		}

		"test request get json event" {
			val asyncParser = ObjectMapper().also {
				it.registerModule(
					KotlinModule.Builder()
						.build()
				)
			}.createNonBlockingByteArrayParser()

			val bytes = httpClient.uri("https://jsonplaceholder.typicode.com/posts").method(io.icure.asyncjacksonhttpclient.net.web.HttpMethod.GET).retrieve().toBytesFlow()
			val jsonEvents = bytes.toJsonEvents(asyncParser).toList()
			assertEquals(StartArray, jsonEvents.first(), "Should start with StartArray")
			assertEquals(StartObject, jsonEvents[1], "jsonEvents[1] == StartObject")
			assertEquals(EndArray, jsonEvents.last(), "Should end with EndArray")
		}

		"test queryViewIncludeDocs" {
			val designDocId = "${codeDAO.designDocId(client)}"
			val limit = 5
			val query = ViewQuery()
				.designDocId(designDocId)
				.viewName("all")
				.limit(limit)
				.includeDocs(true)
			val flow = client.queryViewIncludeDocs<String, String, Code>(query)
			val codes = flow.toList()
			assertEquals(limit, codes.size)
		}

		"test queryViewNoDocs" {
			val designDocId = "${codeDAO.designDocId(client)}"
			val limit = 5
			val query = ViewQuery()
				.designDocId(designDocId)
				.viewName("all")
				.limit(limit)
				.includeDocs(false)
			val flow = client.queryView<String, String>(query)
			val codes = flow.toList()
			assertEquals(limit, codes.size)
		}

		"test raw clientQuery" {
			val designDocId = "${codeDAO.designDocId(client)}"
			val limit = 5
			val query = ViewQuery()
				.designDocId(designDocId)
				.viewName("all")
				.limit(limit)
				.includeDocs(false)
			val flow = client.queryView(query, String::class.java, String::class.java, Nothing::class.java)

			val events = flow.toList()
			assertEquals(1, events.filterIsInstance<TotalCount>().size)
			assertEquals(1, events.filterIsInstance<Offset>().size)
			assertEquals(limit, events.filterIsInstance<ViewRow<*, *, *>>().size)
		}

		"test client get non existing" {
			val nonExistingId = UUID.randomUUID().toString()
			val code = client.get<Code>(nonExistingId)
			Assertions.assertNull(code)
		}

		"test client create and get" {
			val randomCode = UUID.randomUUID().toString()
			val toCreate = Code.from("test", randomCode, "test")
			val created = client.create(toCreate)
			assertEquals(randomCode, created.code)
			Assertions.assertNotNull(created.id)
			Assertions.assertNotNull(created.rev)
			val fetched = checkNotNull(client.get<Code>(created.id)) { "Code was just created, it should exist" }
			assertEquals(fetched.id, created.id)
			assertEquals(fetched.code, created.code)
			assertEquals(fetched.rev, created.rev)
		}

		"test client update" {
			val randomCode = UUID.randomUUID().toString()
			val toCreate = Code.from("test", randomCode, "test")
			val created = client.create(toCreate)
			assertEquals(randomCode, created.code)
			Assertions.assertNotNull(created.id)
			Assertions.assertNotNull(created.rev)
			// update code
			val anotherRandomCode = UUID.randomUUID().toString()
			val updated = client.update(created.copy(code = anotherRandomCode))
			assertEquals(created.id, updated.id)
			assertEquals(anotherRandomCode, updated.code)
			assertNotEquals(created.rev, updated.rev)
			val fetched = checkNotNull(client.get<Code>(updated.id))
			assertEquals(fetched.id, updated.id)
			assertEquals(fetched.code, updated.code)
			assertEquals(fetched.rev, updated.rev)
		}

		"test client update outdated" {
			val randomCode = UUID.randomUUID().toString()
			val toCreate = Code.from("test", randomCode, "test")
			val created = client.create(toCreate)
			assertEquals(randomCode, created.code)
			Assertions.assertNotNull(created.id)
			Assertions.assertNotNull(created.rev)
			// update code
			val anotherRandomCode = UUID.randomUUID().toString()
			val updated = client.update(created.copy(code = anotherRandomCode))
			assertEquals(created.id, updated.id)
			assertEquals(anotherRandomCode, updated.code)
			assertNotEquals(created.rev, updated.rev)
			val fetched = checkNotNull(client.get<Code>(updated.id))
			assertEquals(fetched.id, updated.id)
			assertEquals(fetched.code, updated.code)
			assertEquals(fetched.rev, updated.rev)
			// Should throw a Document update conflict Exception
			@Suppress("UNUSED_VARIABLE")
			shouldThrow<CouchDbConflictException> {
				client.update(created)
			}
		}

		"test client delete" {
			val randomCode = UUID.randomUUID().toString()
			val toCreate = Code.from("test", randomCode, "test")
			val created = client.create(toCreate)
			assertEquals(randomCode, created.code)
			Assertions.assertNotNull(created.id)
			Assertions.assertNotNull(created.rev)
			val deletedRev = client.delete(created)
			assertNotEquals(created.rev, deletedRev)
			Assertions.assertNull(client.get<Code>(created.id))
		}

		"test client bulkget" {
			val designDocId = "${codeDAO.designDocId(client)}"
			val limit = 100
			val query = ViewQuery()
				.designDocId(designDocId)
				.viewName("by_language_type_label")
				.limit(limit)
				.includeDocs(true)
			val flow = client.queryViewIncludeDocs<List<*>, Int, Code>(query)
			val codes = flow.map { it.doc }.toList()
			val codeIds = codes.map { it.id }
			val flow2 = client.get<Code>(codeIds)
			val codes2 = flow2.toList()
			assertEquals(codes, codes2)
		}

		"test client bulkupdate" {
			val testSize = 100
			val codes = List(testSize) { Code.from("test", UUID.randomUUID().toString(), "test") }
			val updateResult = client.bulkUpdate(codes).toList()
			assertEquals(testSize, updateResult.size)
			Assertions.assertTrue(updateResult.all { it.error == null })
			val revisions = updateResult.map { checkNotNull(it.rev) }
			val ids = codes.map { it.id }
			val codeCodes = codes.map { it.code }
			val fetched = client.get<Code>(ids).toList()
			assertEquals(codeCodes, fetched.map { it.code })
			assertEquals(revisions, fetched.map { it.rev })
		}
	}
}
