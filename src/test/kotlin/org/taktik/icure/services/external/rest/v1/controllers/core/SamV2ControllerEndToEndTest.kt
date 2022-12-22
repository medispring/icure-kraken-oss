package org.taktik.icure.services.external.rest.v1.controllers.core

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.SingletonSupport
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.taktik.icure.services.external.rest.v1.dto.MaintenanceTaskDto
import org.taktik.icure.services.external.rest.v1.dto.embed.TaskStatusDto
import org.taktik.icure.test.ICureTestApplication
import reactor.netty.http.client.HttpClient
import java.nio.charset.StandardCharsets
import java.util.*
import org.taktik.icure.services.external.rest.v1.dto.PaginatedList
import org.taktik.icure.services.external.rest.v1.dto.samv2.AmpDto

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
	],
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("app")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SamV2ControllerEndToEndTest(
	@LocalServerPort val port: Int,
	@Autowired val objectMapper: ObjectMapper
) : StringSpec({

	val apiHost = System.getenv("ICURE_BE_URL") ?: "http://localhost"
	val apiEndpoint = "/rest/v1/be_samv2"

	fun getAmpsByLabels(label: String, expectedCode: Int): PaginatedList<AmpDto>? {
		val auth = "Basic ${Base64.getEncoder().encodeToString("${ICureTestApplication.masterHcp!!.login}:${ICureTestApplication.masterHcp!!.password}".toByteArray())}"
		val client = HttpClient.create().headers { h ->
			h.set("Authorization", auth) //
			h.set("Content-type", "application/json")
		}

		val responseBody = client
			.get()
			.uri("$apiHost:$port$apiEndpoint/amp?label=$label")
			.responseSingle { response, buffer ->
				response shouldNotBe null
				response.status().code() shouldBe expectedCode
				buffer.asString(StandardCharsets.UTF_8)
			}.block()

		return responseBody?.let { objectMapper.readValue<PaginatedList<AmpDto>>(it) }
	}

	"Get amps with a label shorter than 3 characters - Failure" {
		val responseBody = getAmpsByLabels("gl", 400)
		responseBody shouldBe null
	}
})
