package org.taktik.icure.security

import java.nio.charset.StandardCharsets
import java.util.Base64
import java.util.UUID
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles
import org.taktik.icure.asyncdao.DeviceDAO
import org.taktik.icure.asyncdao.HealthcarePartyDAO
import org.taktik.icure.asyncdao.PatientDAO
import org.taktik.icure.asyncdao.UserDAO
import org.taktik.icure.entities.Device
import org.taktik.icure.entities.HealthcareParty
import org.taktik.icure.entities.Patient
import org.taktik.icure.entities.User
import org.taktik.icure.entities.security.AlwaysPermissionItem
import org.taktik.icure.entities.security.Permission
import org.taktik.icure.entities.security.PermissionItem
import org.taktik.icure.entities.security.PermissionType
import org.taktik.icure.security.jwt.JwtDetails
import org.taktik.icure.security.jwt.JwtResponse
import org.taktik.icure.security.jwt.JwtUtils
import org.taktik.icure.services.external.rest.v1.dto.AuthenticationResponse
import org.taktik.icure.test.ICureTestApplication
import org.taktik.icure.test.UserCredentials
import org.taktik.icure.test.createDeviceUser
import org.taktik.icure.test.createHcpUser
import org.taktik.icure.test.createPatientUser
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.netty.ByteBufFlux
import reactor.netty.http.client.HttpClient

private const val TEST_CACHE = "build/tests/icureCache"

data class ResponseWithJwt (
	val token: String?,
	val refreshToken: String?
)

fun JwtResponse.toResponseWithJwt() = ResponseWithJwt(this.token, this.refreshToken)
fun AuthenticationResponse.toResponseWithJwt() = ResponseWithJwt(this.token, this.refreshToken)

data class AuthenticationUsers(
	val hcp: UserCredentials,
	val patient: UserCredentials,
	val device: UserCredentials,
	val admin: UserCredentials
)

fun makePostRequestAndExpectResult(payload: String, url: String, expectedCode: Int, headers: Map<String, String> = mapOf(), responseHeaders: List<String> = listOf()): String? {
	val client = HttpClient.create().headers { h ->
		h.set("Content-type", "application/json")
		headers.forEach { (k, v) -> h.set(k, v) }
	}

	val responseBody = client
		.post()
		.uri(url)
		.send(ByteBufFlux.fromString(Flux.just(payload)))
		.responseSingle { response, buffer ->
			response shouldNotBe null
			response.status().code() shouldBe expectedCode
			responseHeaders.onEach {
				response.responseHeaders()[it] shouldNotBe null
			}
			if (response.status().code() == 200) buffer.asString(StandardCharsets.UTF_8)
			else Mono.empty()
		}.block()

	return responseBody
}

fun makePostRequestAndReturnSession(payload: String, url: String, expectedCode: Int, headers: Map<String, String> = mapOf()): String? {
	val client = HttpClient.create().headers { h ->
		h.set("Content-type", "application/json")
		headers.forEach { (k, v) -> h.set(k, v) }
	}

	val responseBody = client
		.post()
		.uri(url)
		.send(ByteBufFlux.fromString(Flux.just(payload)))
		.responseSingle { response, _ ->
			response shouldNotBe null
			response.status().code() shouldBe expectedCode
			response.responseHeaders()["Set-Cookie"] shouldNotBe null
			if (response.status().code() == 200) Mono.just(response.responseHeaders()["Set-Cookie"])
			else Mono.empty()
		}.block()

	return responseBody
}

fun makeGetRequestAndExpectResult(url: String, expectedCode: Int, headers: Map<String, String> = mapOf(), responseHeaders: List<String> = listOf()): String? {
	val client = HttpClient.create().headers { h ->
		h.set("Content-type", "application/json")
		headers.forEach { (k, v) -> h.set(k, v) }
	}

	val responseBody = client
		.get()
		.uri(url)
		.responseSingle { response, buffer ->
			response shouldNotBe null
			response.status().code() shouldBe expectedCode
			responseHeaders.onEach {
				response.responseHeaders()[it] shouldNotBe null
			}
			buffer.asString(StandardCharsets.UTF_8)
		}.block()

	return responseBody
}

@SpringBootTest(
	classes = [ICureTestApplication::class],
	properties = [
		"spring.main.allow-bean-definition-overriding=true",
		"icure.objectstorage.icureCloudUrl=test",
		"icure.objectstorage.cacheLocation=$TEST_CACHE",
		"icure.objectstorage.backlogToObjectStorage=true",
		"icure.objectstorage.sizeLimit=1000",
		"icure.objectstorage.migrationDelayMs=1000",
		"icure.auth.jwt.expirationMillis=5000",
		"icure.auth.jwt.refreshExpirationMillis=20000",
		"icure.login-limit.check=500",
		"icure.login-limit.ban=500"
	],
	webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ActiveProfiles("app")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class JWTAuthenticationE2ETest(
	@LocalServerPort val port: Int,
	@Autowired val jwtUtils: JwtUtils,
	@Autowired val userDAO: UserDAO,
	@Autowired val healthcarePartyDAO: HealthcarePartyDAO,
	@Autowired val patientDAO: PatientDAO,
	@Autowired val deviceDAO: DeviceDAO,
	@Autowired val passwordEncoder: PasswordEncoder,
	@Autowired val objectMapper: ObjectMapper
) : StringSpec() {

	val apiVersion = "v2"
	val apiUrl = "${System.getenv("ICURE_URL") ?: "http://localhost"}:$port"

	init {
		runBlocking {
			val client = org.taktik.icure.test.createHttpClient("john", "LetMeIn")
			val hcpUser = createHcpUser(client, apiUrl, passwordEncoder)
			val patientUser = createPatientUser(client, apiUrl, passwordEncoder)
			val deviceUser = createDeviceUser(client, apiUrl, passwordEncoder)
			val adminUser = createHcpUser(client, apiUrl, passwordEncoder)

			val users = AuthenticationUsers(hcpUser, patientUser, deviceUser, adminUser)

			testJwtAuthentication(
				apiUrl,
				jwtUtils,
				userDAO,
				apiVersion,
				objectMapper,
				users,
				listOf("Set-Cookie")
			)
			testSessionAuthentication(
				apiUrl,
				apiVersion,
				objectMapper,
				users
			)
			testBasicAuthentication(
				apiUrl,
				apiVersion,
				users,
				listOf("Set-Cookie")
			)
		}
	}

}

private fun StringSpec.testBasicAuthentication(
	apiUrl: String,
	apiVersion: String,
	users: AuthenticationUsers,
	responseHeaders: List<String>
) {

	val baseUrl = "$apiUrl/rest/$apiVersion"

	"A User with valid credentials should be able to access endpoints and receive a session cookie" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			200,
			mapOf("Authorization" to "Basic ${Base64.getEncoder().encodeToString("${users.patient.username}:${users.patient.password}".toByteArray())}"),
			responseHeaders
		)
	}

	"A User with non-valid credentials should not be able to access endpoints" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			401,
			mapOf("Authorization" to "Basic ${Base64.getEncoder().encodeToString("${users.patient.username}:INVALID_PASSWORD".toByteArray())}")
		)
	}

	"A User should not be able to access methods with a non-valid Basic authentication string" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			401,
			mapOf("Authorization" to "Basic ${Base64.getEncoder().encodeToString("DOOMED_TO_FAIL_DONT_PANIC".toByteArray())}")
		)
	}

	"A User should not be able to access methods without authentication" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			401
		)
	}

	"A User should be able to get users as a patient" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/user/${users.patient.userId}",
			200,
			mapOf("Authorization" to "Basic ${Base64.getEncoder().encodeToString("${users.patient.username}:${users.patient.password}".toByteArray())}"),
			responseHeaders
		)
	}

	"A User should be able to get users by email as a patient" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/user/byEmail/${users.patient.username}",
			200,
			mapOf("Authorization" to "Basic ${Base64.getEncoder().encodeToString("${users.patient.username}:${users.patient.password}".toByteArray())}"),
			responseHeaders
		)
	}

	"A User should be able to get patients as a patient" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/patient/${users.patient.dataOwnerId}",
			200,
			mapOf("Authorization" to "Basic ${Base64.getEncoder().encodeToString("${users.patient.username}:${users.patient.password}".toByteArray())}"),
			responseHeaders
		)
	}

	"A User should be able to get codes as a patient" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			200,
			mapOf("Authorization" to "Basic ${Base64.getEncoder().encodeToString("${users.patient.username}:${users.patient.password}".toByteArray())}"),
			responseHeaders
		)
	}

	"A User should be able to get codes as an admin" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			200,
			mapOf("Authorization" to "Basic ${Base64.getEncoder().encodeToString("${users.admin.username}:${users.admin.password}".toByteArray())}"),
			responseHeaders
		)
	}

	"A User should not be able to get codes as a device" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			200,
			mapOf("Authorization" to "Basic ${Base64.getEncoder().encodeToString("${users.device.username}:${users.device.password}".toByteArray())}"),
			responseHeaders
		)
	}

	"A User should be able to get patients as a device" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/patient/${users.patient.dataOwnerId}",
			200,
			mapOf("Authorization" to "Basic ${Base64.getEncoder().encodeToString("${users.device.username}:${users.device.password}".toByteArray())}"),
			responseHeaders
		)
	}

	"A User should be able to get patients as a hcp" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/patient/${users.patient.dataOwnerId}",
			200,
			mapOf("Authorization" to "Basic ${Base64.getEncoder().encodeToString("${users.hcp.username}:${users.hcp.password}".toByteArray())}"),
			responseHeaders
		)
	}

	"A User should be able to get HCPs as a patient" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/hcparty/${users.hcp.dataOwnerId}",
			200,
			mapOf("Authorization" to "Basic ${Base64.getEncoder().encodeToString("${users.patient.username}:${users.patient.password}".toByteArray())}"),
			responseHeaders
		)
	}

}

private fun StringSpec.testSessionAuthentication(
	apiUrl: String,
	apiVersion: String,
	objectMapper: ObjectMapper,
	users: AuthenticationUsers
) {

	val baseUrl = "$apiUrl/rest/$apiVersion"

	fun authenticateAndExpectSuccess(username: String, password: String): String {
		val body = objectMapper.writeValueAsString(mapOf("username" to username, "password" to password))
		val responseString = makePostRequestAndReturnSession(body, "${baseUrl}/auth/login", 200, mapOf())
		responseString shouldNotBe null
		val sessionCookie = Regex(".*(SESSION=[a-z0-9\\-]+).*").find(responseString!!)?.groupValues?.get(1)
		sessionCookie shouldNotBe null
		return sessionCookie!!
	}

	"A User with valid credentials should be able to receive a session cookie" {
		authenticateAndExpectSuccess(users.hcp.username, users.hcp.password)
	}

	"A User with non-valid credentials should not be able to receive a session cookie" {
		val body = objectMapper.writeValueAsString(mapOf("username" to users.hcp.username, "password" to "DEFINITELY_NOT_THE_PASSWORD"))
		makePostRequestAndExpectResult(body, "${baseUrl}/auth/login", 401)
	}

	"A User should not be able to access methods with a non-valid session cookie" {
		val sessionCookie = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			401,
			mapOf("Cookie" to "${sessionCookie}and other stuff")
		)
	}

	"A User should not be able to access methods without a session cookie" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			401
		)
	}

	"A User should not be able to access methods with the same session cookie after logout" {
		val sessionCookie = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/auth/logout",
			200,
			mapOf("Cookie" to sessionCookie)
		)
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			403,
			mapOf("Cookie" to sessionCookie)
		)
	}

	"A User should be able to access users with a patient session cookie" {
		val sessionCookie = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/user/${users.patient.userId}",
			200,
			mapOf("Cookie" to sessionCookie)
		)
	}

	"A User should be able to get users by email with a session cookie" {
		val sessionCookie = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/user/byEmail/${users.patient.username}",
			200,
			mapOf("Cookie" to sessionCookie)
		)
	}

	"A User should be able to access patients with a session cookie" {
		val sessionCookie = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/patient/${users.patient.dataOwnerId}",
			200,
			mapOf("Cookie" to sessionCookie)
		)
	}

	"A User should be able to get codes patient session cookie" {
		val sessionCookie = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			200,
			mapOf("Cookie" to sessionCookie)
		)
	}

	"A User should be able to get codes with an admin session cookie" {
		val sessionCookie = authenticateAndExpectSuccess(users.admin.username, users.admin.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			200,
			mapOf("Cookie" to sessionCookie)
		)
	}

	"A User should be able to get patients with a device session cookie" {
		val sessionCookie = authenticateAndExpectSuccess(users.device.username, users.device.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/patient/${users.patient.dataOwnerId}",
			200,
			mapOf("Cookie" to sessionCookie)
		)
	}

	"A User should be able to access patients with a hcp session cookie" {
		val sessionCookie = authenticateAndExpectSuccess(users.hcp.username, users.hcp.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/patient/${users.patient.dataOwnerId}",
			200,
			mapOf("Cookie" to sessionCookie)
		)
	}

	"A User should be able to access hcps with a patient session cookie" {
		val sessionCookie = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/hcparty/${users.hcp.dataOwnerId}",
			200,
			mapOf("Cookie" to sessionCookie)
		)
	}

}

private suspend fun StringSpec.testJwtAuthentication(
	apiUrl: String,
	jwtUtils: JwtUtils,
	userDAO: UserDAO,
	apiVersion: String,
	objectMapper: ObjectMapper,
	users: AuthenticationUsers,
	responseHeaders: List<String>
) {

	val baseUrl = "$apiUrl/rest/$apiVersion"

	fun authenticateAndExpectSuccess(username: String, password: String): ResponseWithJwt {
		val body = objectMapper.writeValueAsString(mapOf("username" to username, "password" to password))
		val responseString = makePostRequestAndExpectResult(body, "${baseUrl}/auth/login", 200, mapOf(), responseHeaders)

		val response = if (apiVersion == "v1")
			objectMapper.readValue(responseString!!, object : TypeReference<AuthenticationResponse>() {})
		else objectMapper.readValue(responseString!!, object : TypeReference<JwtResponse>() {})

		return when (response) {
			is JwtResponse -> {
				response.successful shouldBe true
				response.token shouldNotBe null
				response.refreshToken shouldNotBe null
				response.toResponseWithJwt()
			}

			is AuthenticationResponse -> {
				response.successful shouldBe true
				response.token shouldNotBe null
				response.refreshToken shouldNotBe null
				response.username shouldBe username
				response.toResponseWithJwt()
			}

			else -> throw IllegalStateException("Response type not valid")
		}
	}

	fun regenerateAuthenticationToken(refreshToken: String): JwtResponse {
		val responseString = makePostRequestAndExpectResult("", "${baseUrl}/auth/refresh", 200, mapOf(
			"Refresh-Token" to refreshToken
		))
		val jwtResponse = objectMapper.readValue(responseString!!, object : TypeReference<JwtResponse>() {})
		jwtResponse.successful shouldBe true
		jwtResponse.token shouldNotBe null
		jwtResponse.refreshToken shouldBe null
		return jwtResponse
	}

	"A User with valid credentials should be able to receive an authentication and refresh token" {
		val authResponse = authenticateAndExpectSuccess(users.hcp.username, users.hcp.password)
		val user = userDAO.findUserOnUserDb(users.hcp.userId, true)
		user shouldNotBe null
		val authClaims = jwtUtils.decodeAndGetClaims(authResponse.token!!)
		authClaims["userId"] shouldBe user!!.id
		authClaims["dataOwnerId"] shouldBe (user.patientId ?: user.healthcarePartyId ?: user.deviceId)
		authClaims["dataOwnerType"] shouldBe (
			if (user.patientId != null) JwtDetails.DATA_OWNER_PATIENT
			else if (user.healthcarePartyId != null) JwtDetails.DATA_OWNER_HCP
			else if (user.deviceId != null) JwtDetails.DATA_OWNER_DEVICE
			else ""
			)
		val refreshClaims = jwtUtils.decodeAndGetRefreshClaims(authResponse.refreshToken!!)
		refreshClaims["userId"] shouldBe user.id
	}

	"If both JWT and session cookie are provided, the user should still be able of accessing the method" {
		val body = objectMapper.writeValueAsString(mapOf("username" to users.hcp.username, "password" to users.hcp.password))
		val responseString = makePostRequestAndReturnSession(body, "${baseUrl}/auth/login", 200, mapOf())
		responseString shouldNotBe null
		val sessionCookie = Regex(".*(SESSION=[a-z0-9\\-]+).*").find(responseString!!)?.groupValues?.get(1)
		sessionCookie shouldNotBe null
		val authResponse = authenticateAndExpectSuccess(users.hcp.username, users.hcp.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			200,
			mapOf(
				"Authorization" to "Bearer ${authResponse.token}",
				"Cookie" to sessionCookie!!
			)
		)
	}

	"If an non valid JWT and a valid session cookie are provided, the user should not be able to access the method" {
		val body = objectMapper.writeValueAsString(mapOf("username" to users.hcp.username, "password" to users.hcp.password))
		val responseString = makePostRequestAndReturnSession(body, "${baseUrl}/auth/login", 200, mapOf())
		responseString shouldNotBe null
		val sessionCookie = Regex(".*(SESSION=[a-z0-9\\-]+).*").find(responseString!!)?.groupValues?.get(1)
		sessionCookie shouldNotBe null
		val authResponse = authenticateAndExpectSuccess(users.hcp.username, users.hcp.password)
		delay(10*1000)
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			401,
			mapOf(
				"Authorization" to "Bearer ${authResponse.token}",
				"Cookie" to sessionCookie!!
			)
		)
	}

	"If a valid JWT and a non valid session cookie are provided, the user should be able to access the method" {
		val body = objectMapper.writeValueAsString(mapOf("username" to users.hcp.username, "password" to users.hcp.password))
		val responseString = makePostRequestAndReturnSession(body, "${baseUrl}/auth/login", 200, mapOf())
		responseString shouldNotBe null
		val sessionCookie = Regex(".*(SESSION=[a-z0-9\\-]+).*").find(responseString!!)?.groupValues?.get(1)
		sessionCookie shouldNotBe null
		val authResponse = authenticateAndExpectSuccess(users.hcp.username, users.hcp.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			200,
			mapOf(
				"Authorization" to "Bearer ${authResponse.token}",
				"Cookie" to "${sessionCookie}andotherstuff"
			)
		)
	}

	"A User with non-valid credentials should not be able to receive an authentication and refresh token" {
		val body = objectMapper.writeValueAsString(mapOf("username" to users.hcp.username, "password" to "DEFINITELY_NOT_THE_PASSWORD"))
		makePostRequestAndExpectResult(body, "${baseUrl}/auth/login", 401)
	}

	"A User should not be able to access methods with a malformed token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			401,
			mapOf("Authorization" to "Bearer ${authResponse.token}plusone")
		)
	}

	"A User should not be able to change their role by altering the token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		println(authResponse.token!!)
		val (header, payload, signature) = authResponse.token.split('.')
		val decodedPayload = Base64.getDecoder().decode(payload).toString(Charsets.UTF_8)
		val tamperedPayload = Base64.getEncoder().encodeToString(decodedPayload.replace("ROLE_PATIENT", "ROLE_ADMIN").toByteArray())
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			401,
			mapOf("Authorization" to "Bearer ${header}.${tamperedPayload}.${signature}")
		)
	}

	"A User should not be able to access methods with an invalid token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			401,
			mapOf("Authorization" to "Bearer ${authResponse.token?.replace('h', 'f')}")
		)
	}

	"A User should not be able to access methods without a token" {
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			401,
			mapOf("Authorization" to "Bearer ")
		)
	}

	"A User should not be able to access methods with an expired token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		delay(10*1000)
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			401,
			mapOf("Authorization" to "Bearer ${authResponse.token}")
		)
	}

	"A User should not be able to access methods with a refresh token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			401,
			mapOf("Authorization" to "Bearer ${authResponse.refreshToken}")
		)
	}

	"A User should be able to get a new authentication token using the refresh token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		delay(10*1000)
		val newAuth = regenerateAuthenticationToken(authResponse.refreshToken!!)
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			200,
			mapOf("Authorization" to "Bearer ${newAuth.token}")
		)
	}

	"A User should not be able to get a new authentication token using a malformed refresh token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makePostRequestAndExpectResult(
			"",
			"${baseUrl}/auth/refresh",
			401,
			mapOf("Refresh-Token" to "${authResponse.refreshToken}TRAIL"))
	}

	"A User should not be able to get a new authentication token using a non-valid refresh token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makePostRequestAndExpectResult(
			"",
			"${baseUrl}/auth/refresh",
			401,
			mapOf("Refresh-Token" to authResponse.refreshToken!!.replace('y', 'e')))
	}

	"A User should not be able to get a new authentication token using an authentication token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makePostRequestAndExpectResult(
			"",
			"${baseUrl}/auth/refresh",
			401,
			mapOf("Refresh-Token" to authResponse.token!!))
	}

	"A User should not be able to get a new authentication token using an expired refresh token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		delay(30*1000)
		makePostRequestAndExpectResult(
			"",
			"${baseUrl}/auth/refresh",
			401,
			mapOf("Refresh-Token" to authResponse.refreshToken!!))
	}

	"If the user performs the login again, a new valid refresh token is issued and the old one stays valid" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makePostRequestAndExpectResult(
			"",
			"${baseUrl}/auth/refresh",
			200,
			mapOf("Refresh-Token" to authResponse.refreshToken!!))
	}

	"If the user performs the login again, a new valid refresh token is issued and the new one stays valid" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		val newResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makePostRequestAndExpectResult(
			"",
			"${baseUrl}/auth/refresh",
			200,
			mapOf("Refresh-Token" to newResponse.refreshToken!!))
	}

	"If the user calls the invalidate method with a valid refresh token, the refresh token is invalidated" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makePostRequestAndExpectResult(
			"",
			"${baseUrl}/auth/invalidate",
			200,
			mapOf("Refresh-Token" to authResponse.refreshToken!!))

		makePostRequestAndExpectResult(
			"",
			"${baseUrl}/auth/refresh",
			401,
			mapOf("Refresh-Token" to authResponse.refreshToken!!))
	}

	"If the user calls the invalidate method with a valid refresh token, the other refresh tokens stays valid" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		val secondAuth = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makePostRequestAndExpectResult(
			"",
			"${baseUrl}/auth/invalidate",
			200,
			mapOf("Refresh-Token" to authResponse.refreshToken!!))

		makePostRequestAndExpectResult(
			"",
			"${baseUrl}/auth/refresh",
			200,
			mapOf("Refresh-Token" to secondAuth.refreshToken!!))
	}

	"A User should not be able of invalidating a refresh token with a non-valid refresh token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makePostRequestAndExpectResult(
			"",
			"${baseUrl}/auth/invalidate",
			401,
			mapOf("Refresh-Token" to "${authResponse.refreshToken}-nope")
		)
	}

	"A User should not be able of invalidating a refresh token with an expired refresh token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		delay(30000)
		makePostRequestAndExpectResult(
			"",
			"${baseUrl}/auth/invalidate",
			401,
			mapOf("Refresh-Token" to "${authResponse.refreshToken}-nope")
		)
	}

	"A User should be able to get the current user with an authentication token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/user/${users.patient.userId}",
			200,
			mapOf("Authorization" to "Bearer ${authResponse.token}")
		)
	}

	"A User should be able to get a user with an authentication token for another user" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/user/${users.hcp.userId}",
			200,
			mapOf("Authorization" to "Bearer ${authResponse.token}")
		)
	}

	"A User should be able to get a user by email an authentication token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/user/byEmail/${users.patient.username}",
			200,
			mapOf("Authorization" to "Bearer ${authResponse.token}")
		)
	}

	"A User should not be able to get a user by email with an authentication token for another user" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/user/byEmail/${users.hcp.username}",
			200,
			mapOf("Authorization" to "Bearer ${authResponse.token}")
		)
	}

	"A User should be able to get a patient with a patient authentication token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/patient/${users.patient.dataOwnerId}",
			200,
			mapOf("Authorization" to "Bearer ${authResponse.token}")
		)
	}

	"A User should be able to get the codes with a patient authentication token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			200,
			mapOf("Authorization" to "Bearer ${authResponse.token}")
		)
	}

	"A User should be able to get the codes with an admin authentication token" {
		val authResponse = authenticateAndExpectSuccess(users.admin.username, users.admin.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/code?region=be",
			200,
			mapOf("Authorization" to "Bearer ${authResponse.token}")
		)
	}

	"A User should not be able to get a patient with a device authentication token " {
		val authResponse = authenticateAndExpectSuccess(users.device.username, users.device.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/patient/${users.patient.dataOwnerId}",
			200,
			mapOf("Authorization" to "Bearer ${authResponse.token}")
		)
	}

	"A User should be able to get a patient with a HCP authentication token" {
		val authResponse = authenticateAndExpectSuccess(users.hcp.username, users.hcp.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/patient/${users.patient.dataOwnerId}",
			200,
			mapOf("Authorization" to "Bearer ${authResponse.token}")
		)
	}

	"A User should be able to get a HCP with a patient authentication token" {
		val authResponse = authenticateAndExpectSuccess(users.patient.username, users.patient.password)
		makeGetRequestAndExpectResult(
			"${baseUrl}/hcparty/${users.hcp.dataOwnerId}",
			200,
			mapOf("Authorization" to "Bearer ${authResponse.token}")
		)
	}

}
