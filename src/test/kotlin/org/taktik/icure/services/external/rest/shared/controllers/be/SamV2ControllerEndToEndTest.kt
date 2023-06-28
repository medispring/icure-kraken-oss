package org.taktik.icure.services.external.rest.shared.controllers.be

import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.zip.GZIPInputStream
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import io.netty.buffer.Unpooled
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.taktik.icure.asyncdao.UserDAO
import org.taktik.icure.properties.CouchDbProperties
import org.taktik.icure.services.external.rest.v1.dto.PaginatedList
import org.taktik.icure.services.external.rest.v1.dto.samv2.AmpDto
import org.taktik.icure.test.ICureTestApplication
import org.taktik.icure.test.TestProperties
import org.taktik.icure.test.createHcpUser
import org.taktik.icure.test.createHttpClient
import org.taktik.icure.test.createPatientUser
import org.taktik.icure.test.makeGetRequest
import org.taktik.icure.test.uuid
import reactor.core.publisher.Flux
import reactor.netty.http.client.HttpClient

@SpringBootTest(
	classes = [ICureTestApplication::class],
	properties = [
		"spring.main.allow-bean-definition-overriding=true",
		"icure.sync.global.databases=true",
	],
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("app")
@EnableAutoConfiguration
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SamV2ControllerEndToEndTest(
	@LocalServerPort val port: Int,
	@Autowired val passwordEncoder: PasswordEncoder,
	@Autowired val objectMapper: ObjectMapper,
	@Autowired val testProperties: TestProperties,
) : StringSpec() {

	val apiUrl = "${System.getenv("ICURE_URL") ?: "http://localhost"}:$port"

	init {
		runBlocking {
			val client = createHttpClient(ICureTestApplication.masterHcp!!.username, ICureTestApplication.masterHcp!!.password)

			val hcpUser = createHcpUser(client, apiUrl, passwordEncoder)
			val hcpClient = createHttpClient(hcpUser.userId, hcpUser.password)

			val patientUser = createPatientUser(client, apiUrl, passwordEncoder)
			val patientClient = createHttpClient(patientUser.userId, patientUser.password)

			uploadTestData()

			listOf("v1", "v2").forEach { v ->
				findAmpsByLabelE2ETest(hcpClient, patientClient, objectMapper, apiUrl, v)
				findAmpsByCodeE2ETest(hcpClient, patientClient, objectMapper, apiUrl, v)
			}
		}
	}

	private fun StringSpec.findAmpsByLabelE2ETest(
		hcpClient: HttpClient,
		patientClient: HttpClient,
		objectMapper: ObjectMapper,
		apiUrl: String,
		apiVersion: String
	) {

		"$apiVersion - An HCP can get Amps by label" {
			val label = "filmtabl"

			val response = makeGetRequest(
				hcpClient,
				"$apiUrl/rest/$apiVersion/be_samv2/amp?language=de&label=$label",
			).shouldNotBeNull().let { objectMapper.readValue<PaginatedList<AmpDto>>(it) }

			response.rows.onEach {
				it.prescriptionName?.de?.lowercase() shouldContain label
			}.size shouldBeGreaterThan 0
		}

		"$apiVersion - An HCP can get Amps by label with pagination" {
			val label = "filmtabl"
			val limit = 4

			val responseFirstPage = makeGetRequest(
				hcpClient,
				"$apiUrl/rest/$apiVersion/be_samv2/amp?language=de&label=$label&limit=$limit",
			).shouldNotBeNull().let { objectMapper.readValue<PaginatedList<AmpDto>>(it) }

			responseFirstPage.rows.onEach {
				it.prescriptionName?.de?.lowercase() shouldContain label
			}.size shouldBe limit
			responseFirstPage.nextKeyPair.shouldNotBeNull().startKeyDocId.shouldNotBeNull()
		}

		"$apiVersion - An HCP is provided an empty result if no Amp matching the label is found" {
			val response = makeGetRequest(
				hcpClient,
				"$apiUrl/rest/$apiVersion/be_samv2/amp?language=en&label=${uuid()}",
			).shouldNotBeNull().let { objectMapper.readValue<PaginatedList<AmpDto>>(it) }

			response.rows.size shouldBe 0
		}

		"$apiVersion - A Patient cannot get Amps by label" {
			makeGetRequest(
				patientClient,
				"$apiUrl/rest/$apiVersion/be_samv2/amp?language=en&label=${uuid()}",
				403
			)
		}

		"$apiVersion - A HCP cannot get Amps if the passed label is too short" {
			makeGetRequest(
				hcpClient,
				"$apiUrl/rest/$apiVersion/be_samv2/amp?language=en&label=a",
				400
			)
		}

	}

	private fun StringSpec.findAmpsByCodeE2ETest(
		hcpClient: HttpClient,
		patientClient: HttpClient,
		objectMapper: ObjectMapper,
		apiUrl: String,
		apiVersion: String
	) {

		"$apiVersion - An HCP can get Amps by code" {
			val code = "SAM426605-00"

			val response = makeGetRequest(
				hcpClient,
				"$apiUrl/rest/$apiVersion/be_samv2/amp/byAmpCode/$code",
			).shouldNotBeNull().let { objectMapper.readValue<List<AmpDto>>(it) }

			response.onEach {
				it.code shouldBe code
			}.size shouldBeGreaterThan 0
		}

		"$apiVersion - A Patient cannot get Amps by code" {
			makeGetRequest(
				patientClient,
				"$apiUrl/rest/$apiVersion/be_samv2/amp?language=en&label=SAM426605-00",
				403
			)
		}

	}

	fun uploadTestData() {
		try {
			val auth = "Basic ${Base64.getEncoder().encodeToString("${testProperties.couchdb.username}:${testProperties.couchdb.password}".toByteArray())}"
			val client = HttpClient.create().headers { h ->
				h.set("Authorization", auth) //
				h.set("Content-type", "application/json")
			}

			val res = client
				.post()
				.uri("${testProperties.couchdb.serverUrl}/icure-drugs/_bulk_docs")
				.send(Flux.using(
					{ GZIPInputStream(this::class.java.getResourceAsStream("/org/taktik/icure/be/samv2/amps.json.gz")) },
					{ f ->
						val buffer = ByteArray(4096)
						Flux.generate { sink ->
							val read = f.read(buffer)
							if (read == -1) {
								sink.complete()
							} else {
								sink.next(Unpooled.copiedBuffer(buffer.sliceArray(0 until read)))
							}
						}
					},
					{ it.close() }
				))
				.responseSingle { _, buffer ->
					buffer.asString(StandardCharsets.UTF_8)
				}.block()
			println(res.toString())
		} catch (e: Exception) {
			//ignore
		}
	}
}
