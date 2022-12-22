package org.taktik.icure.services.external.rest.v1.controllers.core

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.SingletonSupport
import kotlinx.coroutines.runBlocking
import org.hibernate.validator.internal.util.Contracts.assertNotNull
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.test.context.ActiveProfiles
import org.taktik.icure.services.external.rest.v1.dto.PaginatedList
import org.taktik.icure.services.external.rest.v1.dto.UserDto
import org.taktik.icure.test.ICureTestApplication
import org.taktik.icure.test.removeEntities
import reactor.core.publisher.Mono
import reactor.netty.ByteBufFlux
import reactor.netty.http.client.HttpClient
import java.util.UUID
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.taktik.icure.test.createHttpClient
import org.taktik.icure.test.makeGetRequest
import org.taktik.icure.test.makePostRequest

@SpringBootTest(
    classes = [ICureTestApplication::class],
    properties = ["spring.main.allow-bean-definition-overriding=true"],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("app")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserControllerIntegrationTest(
	@LocalServerPort val port: Int,
	@Autowired val objectMapper: ObjectMapper
) {
	val apiUrl = System.getenv("ICURE_URL") ?: "http://localhost"
	fun apiUrl() = if (apiUrl == "http://localhost") "$apiUrl:$port" else apiUrl
	val hcpAuth = "Basic ${java.util.Base64.getEncoder().encodeToString("${ICureTestApplication.masterHcp!!.login}:${ICureTestApplication.masterHcp!!.password}".toByteArray())}"
    val client = createHttpClient(hcpAuth)
	lateinit var createdIds: List<String>

    @BeforeAll
    fun addTestUsers() {
        val uuid = {
            UUID.randomUUID().toString()
        }

        val usersToCreate = listOf(
            UserDto(
                id = uuid(),
                email = uuid(),
                patientId = uuid()
            ),
            UserDto(
                id = uuid(),
                email = uuid(),
                patientId = uuid(),
                healthcarePartyId = uuid()
            ),
            UserDto(
                id = uuid(),
                email = uuid(),
                healthcarePartyId = uuid()
            )
        )

        createdIds = usersToCreate.map {
			val result = makePostRequest(
				client,
				"${apiUrl()}/rest/v1/user",
				objectMapper.writeValueAsString(it))
			result shouldNotBe null
			objectMapper.readValue<UserDto>(result!!)
		}.map { it.id }
    }

    /**
     * Also cover:
     * - skipPatients = true, user has a patientId AND a hcpId ==> Should be found
     * - skipPatients = true, user has no patientId AND no hcpId ==> Should be found
     */
    @Test
    fun `Implicit skipPatients = true, user has only a patientId, Should not be found`() {
        val response = makeGetRequest(client, "${apiUrl()}/rest/v1/user")
		response shouldNotBe null
		val responseBody = objectMapper.readValue<PaginatedList<UserDto>>(response!!)
        assertEquals(3, responseBody.rows.size)
        assertTrue(responseBody.rows.all { it.patientId == null || it.healthcarePartyId != null })
    }

    /**
     * Also cover:
     * - skipPatients = true, user has a patientId AND a hcpId ==> Should be found
     * - skipPatients = true, user has no patientId AND no hcpId ==> Should be found
     */
    @Test
    fun `Explicit skipPatients = true, user has only a patientId, Should not be found`() {
		val response = makeGetRequest(client, "${apiUrl()}/rest/v1/user?skipPatients=true")
		response shouldNotBe null
		val responseBody = objectMapper.readValue<PaginatedList<UserDto>>(response!!)
        assertEquals(3, responseBody.rows.size)
        assertTrue(responseBody.rows.all { it.patientId == null || it.healthcarePartyId != null })
    }

    @Test
    fun `skipPatients = false, user has only a patientId, Should be found`() {
		val response = makeGetRequest(client, "${apiUrl()}/rest/v1/user?skipPatients=false")
		response shouldNotBe null
		val responseBody = objectMapper.readValue<PaginatedList<UserDto>>(response!!)
        assertNotNull(responseBody)
        assertEquals(4, responseBody.rows.size)
    }
}
