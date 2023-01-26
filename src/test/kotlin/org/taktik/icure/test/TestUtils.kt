package org.taktik.icure.test

import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import java.security.KeyPairGenerator
import java.util.UUID
import kotlin.math.abs
import kotlin.random.Random.Default.nextInt
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.SingletonSupport
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.junit.jupiter.api.Assertions
import org.springframework.security.crypto.password.PasswordEncoder
import org.taktik.icure.asyncdao.UserDAO
import kotlinx.coroutines.delay
import org.taktik.icure.asyncdao.HealthcarePartyDAO
import org.taktik.icure.constants.Users
import org.taktik.icure.entities.HealthcareParty
import org.taktik.icure.entities.User
import org.taktik.icure.entities.security.AlwaysPermissionItem
import org.taktik.icure.entities.security.Permission
import org.taktik.icure.entities.security.PermissionType
import org.taktik.icure.services.external.rest.v1.dto.DeviceDto
import org.taktik.icure.services.external.rest.v1.dto.HealthElementDto
import org.taktik.icure.services.external.rest.v1.dto.HealthcarePartyDto
import org.taktik.icure.services.external.rest.v1.dto.PatientDto
import org.taktik.icure.services.external.rest.v1.dto.UserDto
import org.taktik.icure.services.external.rest.v1.dto.security.PermissionDto
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.netty.ByteBufFlux
import reactor.netty.http.client.HttpClient

val objectMapper: ObjectMapper by lazy {
	ObjectMapper().registerModule(
		KotlinModule.Builder()
			.nullIsSameAsDefault(nullIsSameAsDefault = false)
			.reflectionCacheSize(reflectionCacheSize = 512)
			.nullToEmptyMap(nullToEmptyMap = false)
			.nullToEmptyCollection(nullToEmptyCollection = false)
			.singletonSupport(singletonSupport = SingletonSupport.DISABLED)
			.strictNullChecks(strictNullChecks = false)
			.build()
	)
}

fun createHttpClient(username: String, password: String, additionalHeaders: Map<String, String> = emptyMap()): HttpClient {
	val auth = "Basic ${java.util.Base64.getEncoder().encodeToString("${username}:${password}".toByteArray())}"
	return createHttpClient(auth, additionalHeaders)
}

fun createHttpClient(userAuth: String, additionalHeaders: Map<String, String> = emptyMap()): HttpClient {
	return HttpClient.create().headers { h ->
		h.set("Authorization", userAuth) //
		h.set("Content-type", "application/json")
		additionalHeaders.forEach {
			h.set(it.key, it.value)
		}
	}
}

fun makeGetRequest(client: HttpClient, url: String, expectedCode: Int = 200, additionalHeaders: Map<String, String> = mapOf()): String? {
	val responseBody = client
		.headers {
			additionalHeaders.forEach { (k, v) ->
				it.set(k, v)
			}
		}
		.get()
		.uri(url)
		.responseSingle { response, buffer ->
			Assertions.assertNotNull(response)
			Assertions.assertEquals(expectedCode, response.status().code())
			buffer.asString(StandardCharsets.UTF_8)
		}.block()

	return responseBody
}

fun makePostRequest(client: HttpClient, url: String, payload: String, expectedCode: Int = 200, additionalHeaders: Map<String, String> = mapOf()): String? {
	val responseBody = client
		.headers {
			additionalHeaders.forEach { (k, v) ->
				it.set(k, v)
			}
		}
		.post()
		.uri(url)
		.send(ByteBufFlux.fromString(Flux.just(payload)))
		.responseSingle { response, buffer ->
			Assertions.assertNotNull(response)
			Assertions.assertEquals(expectedCode, response.status().code())
			buffer.asString(StandardCharsets.UTF_8)
		}.block()

	return responseBody
}

fun makePutRequest(client: HttpClient, url: String, payload: String, expectedCode: Int = 200, additionalHeaders: Map<String, String> = mapOf()): String? {
	val responseBody = client
		.headers {
			additionalHeaders.forEach { (k, v) ->
				it.set(k, v)
			}
		}
		.put()
		.uri(url)
		.send(ByteBufFlux.fromString(Flux.just(payload)))
		.responseSingle { response, buffer ->
			Assertions.assertNotNull(response)
			Assertions.assertEquals(expectedCode, response.status().code())
			buffer.asString(StandardCharsets.UTF_8)
		}.block()

	return responseBody
}

fun makeDeleteRequest(client: HttpClient, url: String, expectedCode: Int = 200, additionalHeaders: Map<String, String> = mapOf()): String? {
	val responseBody = client
		.headers {
			additionalHeaders.forEach { (k, v) ->
				it.set(k, v)
			}
		}
		.delete()
		.uri(url)
		.responseSingle { response, buffer ->
			Assertions.assertNotNull(response)
			Assertions.assertEquals(expectedCode, response.status().code())
			buffer.asString(StandardCharsets.UTF_8)
		}.block()

	return responseBody
}

@JsonIgnoreProperties(ignoreUnknown = true)
private data class IdWithRev(@field:JsonProperty("_id") val id: String, @field:JsonProperty("_rev") val rev: String)

fun generateRandomString(length: Int, alphabet: List<Char>) = (1..length)
	.map { _ -> alphabet[nextInt(0, alphabet.size)] }
	.joinToString("")


private fun ByteArray.keyToHexString() = asUByteArray().joinToString("") { it.toString(16).padStart(2, '0') }

fun createPatientUser(httpClient: HttpClient,
	apiUrl: String,
	passwordEncoder: PasswordEncoder,
	): UserCredentials {
	val username = "pat-${UUID.randomUUID()}"
	val password = UUID.randomUUID().toString()
	val passwordHash = passwordEncoder.encode(password)

	val rsaKeyGenerator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
	val rsaKeypair = rsaKeyGenerator.generateKeyPair()

	val pubKey = rsaKeypair.public.encoded.keyToHexString()
	val privateKey = rsaKeypair.private.encoded.keyToHexString()

	val patientToCreate = PatientDto(
		id = UUID.randomUUID().toString(),
		firstName = "pat",
		lastName = username,
		publicKey = pubKey
	)

	val userToCreate = UserDto(
		id = UUID.randomUUID().toString(),
		login = username,
		email = username,
		passwordHash = passwordHash,
		patientId = patientToCreate.id
	)

	makePostRequest(httpClient, "$apiUrl/rest/v1/patient", objectMapper.writeValueAsString(patientToCreate))
	makePostRequest(httpClient, "$apiUrl/rest/v1/user", objectMapper.writeValueAsString(userToCreate))

	return UserCredentials(
		userToCreate.id,
		patientToCreate.id,
		username,
		password,
		privateKey
	)
}

fun createHcpUser(httpClient: HttpClient,
	apiUrl: String,
	passwordEncoder: PasswordEncoder,
): UserCredentials {
	val username = "hcp-${UUID.randomUUID()}"
	val password = UUID.randomUUID().toString()
	val passwordHash = passwordEncoder.encode(password)

	val rsaKeyGenerator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
	val rsaKeypair = rsaKeyGenerator.generateKeyPair()

	val pubKey = rsaKeypair.public.encoded.keyToHexString()
	val privateKey = rsaKeypair.private.encoded.keyToHexString()

	val hcpToCreate = HealthcarePartyDto(
		id = UUID.randomUUID().toString(),
		firstName = "hcp",
		lastName = username,
		publicKey = pubKey
	)

	val userToCreate = UserDto(
		id = UUID.randomUUID().toString(),
		login = username,
		email = username,
		passwordHash = passwordHash,
		healthcarePartyId = hcpToCreate.id
	)

	makePostRequest(httpClient, "$apiUrl/rest/v1/hcparty", objectMapper.writeValueAsString(hcpToCreate))
	makePostRequest(httpClient, "$apiUrl/rest/v1/user", objectMapper.writeValueAsString(userToCreate))

	return UserCredentials(
		userToCreate.id,
		hcpToCreate.id,
		username,
		password,
		privateKey
	)
}

fun createDeviceUser(httpClient: HttpClient,
	apiUrl: String,
	passwordEncoder: PasswordEncoder,
): UserCredentials {
	val username = "device-${UUID.randomUUID()}"
	val password = UUID.randomUUID().toString()
	val passwordHash = passwordEncoder.encode(password)

	val rsaKeyGenerator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
	val rsaKeypair = rsaKeyGenerator.generateKeyPair()

	val pubKey = rsaKeypair.public.encoded.keyToHexString()
	val privateKey = rsaKeypair.private.encoded.keyToHexString()

	val deviceToCreate = DeviceDto(
		id = UUID.randomUUID().toString(),
		brand = "device",
		name = username,
		publicKey = pubKey
	)

	val userToCreate = UserDto(
		id = UUID.randomUUID().toString(),
		login = username,
		passwordHash = passwordHash,
		deviceId = deviceToCreate.id
	)

	makePostRequest(httpClient, "$apiUrl/rest/v1/device", objectMapper.writeValueAsString(deviceToCreate))
	makePostRequest(httpClient, "$apiUrl/rest/v1/user", objectMapper.writeValueAsString(userToCreate))

	return UserCredentials(
		userToCreate.id,
		deviceToCreate.id,
		username,
		password,
		privateKey
	)
}

suspend fun createHcpUser(
	userDAO: UserDAO,
	healthcarePartyDAO: HealthcarePartyDAO,
	passwordEncoder: PasswordEncoder,
): UserCredentials {
	val username = "hcp-${UUID.randomUUID()}"
	val password = UUID.randomUUID().toString()
	val passwordHash = passwordEncoder.encode(password)

	val rsaKeyGenerator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
	val rsaKeypair = rsaKeyGenerator.generateKeyPair()

	val pubKey = rsaKeypair.public.encoded.keyToHexString()
	val privateKey = rsaKeypair.private.encoded.keyToHexString()

	val healthcarePartyToCreate = HealthcareParty(
		id = UUID.randomUUID().toString(),
		firstName = "hcp",
		lastName = username,
		publicKey = pubKey
	)

	val userToCreate = User(
		id = UUID.randomUUID().toString(),
		login = username,
		status = Users.Status.ACTIVE,
		passwordHash = passwordHash,
		healthcarePartyId = healthcarePartyToCreate.id
	)

	userDAO.create(userToCreate)
	healthcarePartyDAO.create(healthcarePartyToCreate)

	return UserCredentials(
		userToCreate.id,
		healthcarePartyToCreate.id,
		username,
		password,
		privateKey
	)
}

data class UserCredentials(
	val userId: String,
	val dataOwnerId: String,
	val username: String,
	val password: String,
	val privateKey: String?
) {
	fun auth() = "Basic ${java.util.Base64.getEncoder().encodeToString("${username}:${password}".toByteArray())}"

}
