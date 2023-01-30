package org.taktik.icure.controller

import java.util.UUID
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.SingletonSupport
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.taktik.icure.services.external.rest.v1.dto.FormDto
import org.taktik.icure.test.ICureTestApplication
import reactor.core.publisher.Mono
import reactor.netty.ByteBufFlux
import reactor.netty.http.client.HttpClient

private const val TEST_CACHE = "build/tests/icureCache"

@SpringBootTest(
	classes = [ICureTestApplication::class],
	properties = [
	"spring.main.allow-bean-definition-overriding=true",
	"icure.objectstorage.icureCloudUrl=test",
	"icure.objectstorage.cacheLocation=$TEST_CACHE",
	"icure.objectstorage.backlogToObjectStorage=true",
	"icure.objectstorage.sizeLimit=1000",
	"icure.objectstorage.migrationDelayMs=1000",
], webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("app")
class FormControllerTest {
	private val log = LoggerFactory.getLogger(this.javaClass)

	@LocalServerPort
	var port = 0

	@Test
	fun testCreateDeleteBulk() {
		val client = HttpClient.create().headers { h ->
			h.set("Authorization", "Basic ${java.util.Base64.getEncoder().encodeToString("john:LetMeIn".toByteArray())}")
			h.set("Content-type", "application/json")
		}
		val objectMapper = ObjectMapper().registerModule(
			KotlinModule.Builder()
				.nullIsSameAsDefault(nullIsSameAsDefault = false)
				.reflectionCacheSize(reflectionCacheSize = 512)
				.nullToEmptyMap(nullToEmptyMap = false)
				.nullToEmptyCollection(nullToEmptyCollection = false)
				.singletonSupport(singletonSupport = SingletonSupport.DISABLED)
				.strictNullChecks(strictNullChecks = false)
				.build()
		)

		runBlocking {
			val res = client.delete()
				.uri(
					"http://127.0.0.1:$port/rest/v1/form/${
					flow {
						(1..100).map {
							UUID.randomUUID().toString().also {
								val form = objectMapper.writeValueAsString(FormDto(id = it))
								log.info(
									"${
									client.post()
										.uri("http://127.0.0.1:$port/rest/v1/form")
										.send(ByteBufFlux.fromString(Mono.just(form)))
										.response()
										.awaitFirstOrNull()?.status() ?: "000"
									}"
								)
								emit(it)
							}
						}
					}.toList().joinToString(",")
					}"
				)
				.response()
				.awaitFirstOrNull()

			log.info("Status: ${res?.status() ?: "000"}")
		}
	}
}
