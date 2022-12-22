package org.taktik.icure.services.external.rest.v1.dto.filter.user

import kotlin.random.Random.Default.nextInt
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.taktik.icure.asynclogic.UserLogic
import org.taktik.icure.asynclogic.impl.filter.Filters
import org.taktik.icure.services.external.rest.v1.dto.PaginatedList
import org.taktik.icure.services.external.rest.v1.dto.UserDto
import org.taktik.icure.services.external.rest.v1.mapper.UserMapper
import org.taktik.icure.test.ICureTestApplication
import org.taktik.icure.test.createHttpClient
import org.taktik.icure.test.generators.UserGenerator
import org.taktik.icure.test.makePostRequest

@SpringBootTest(
	classes = [ICureTestApplication::class],
	properties = ["spring.main.allow-bean-definition-overriding=true"],
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("app")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsersByPatientIdFilterTest constructor(
	@LocalServerPort val port: Int,
	@Autowired private val objectMapper: ObjectMapper
) {

	val apiUrl = System.getenv("ICURE_URL") ?: "http://localhost"
	fun apiUrl() = if (apiUrl == "http://localhost") "$apiUrl:$port" else apiUrl
	val hcpAuth = "Basic ${java.util.Base64.getEncoder().encodeToString("${ICureTestApplication.masterHcp!!.login}:${ICureTestApplication.masterHcp!!.password}".toByteArray())}"
	val client = createHttpClient(hcpAuth)
	val userGenerator = UserGenerator()
	val users = userGenerator.generateRandomUsers(10)

	init {
		runBlocking {
			users.forEach {
				makePostRequest(
					client,
					"${apiUrl()}/rest/v1/user",
					objectMapper.writeValueAsString(it)) shouldNotBe null
			}
		}
	}

	@Test
	fun usersByPatientIdFilterCanRetrieveASingleUser() {
		runBlocking {
			val user = users[nextInt(0, users.size)]
			assertNotNull(user.patientId)
			val responseFilter = makePostRequest(
				client,
				"${apiUrl()}/rest/v1/user/filter",
				"{\"\$type\":\"FilterChain\",\"filter\":{\"patientId\":\"${user.patientId}\",\"\$type\":\"UsersByPatientIdFilter\"}}")
			responseFilter shouldNotBe null
			val response = objectMapper.readValue<PaginatedList<UserDto>>(responseFilter!!)
			response.rows.size shouldBe 1
			response.rows.first().id shouldBe user.id
		}
	}

	@Test
	fun usersByPatientIdFilterCanRetrieveMultipleUsers() {
		runBlocking {
			val sampleUser = users[nextInt(0, users.size)]
			assertNotNull(sampleUser.patientId)
			val userWithDuplicatedPatientId = userGenerator.generateRandomUsers(1)[0].copy(patientId = sampleUser.patientId)
			makePostRequest(
				client,
				"${apiUrl()}/rest/v1/user",
				objectMapper.writeValueAsString(userWithDuplicatedPatientId)) shouldNotBe null
			val responseFilter = makePostRequest(
				client,
				"${apiUrl()}/rest/v1/user/filter",
				"{\"\$type\":\"FilterChain\",\"filter\":{\"patientId\":\"${sampleUser.patientId}\",\"\$type\":\"UsersByPatientIdFilter\"}}")
			responseFilter shouldNotBe null
			val response = objectMapper.readValue<PaginatedList<UserDto>>(responseFilter!!)
			response.rows.size shouldBe 2
			response.rows.forEach {
				listOf(sampleUser.id, userWithDuplicatedPatientId.id) shouldContain it.id
			}
		}
	}

	@Test
	fun usersByPatientIdReturnsNoUserIfNoResultMatchesPatientId() {
		runBlocking {
			val responseFilter = makePostRequest(
				client,
				"${apiUrl()}/rest/v1/user/filter",
				"{\"\$type\":\"FilterChain\",\"filter\":{\"patientId\":\"I_DO_NOT_EXIST\",\"\$type\":\"UsersByPatientIdFilter\"}}")
			responseFilter shouldNotBe null
			val response = objectMapper.readValue<PaginatedList<UserDto>>(responseFilter!!)
			response.rows.size shouldBe 0
		}
	}
}
