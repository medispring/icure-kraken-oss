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
import org.taktik.icure.asyncdao.PatientDAO
import org.taktik.icure.asyncdao.UserDAO
import org.taktik.icure.entities.Patient
import org.taktik.icure.entities.User
import org.taktik.icure.services.external.rest.v1.dto.PatientDto
import org.taktik.icure.services.external.rest.v1.dto.UserDto
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

fun makeGetRequest(client: HttpClient, url: String, expectedCode: Int = 200): String? {
	val responseBody = client
		.get()
		.uri(url)
		.responseSingle { response, buffer ->
			Assertions.assertNotNull(response)
			Assertions.assertEquals(expectedCode, response.status().code())
			buffer.asString(StandardCharsets.UTF_8)
		}.block()

	return responseBody
}

fun makePostRequest(client: HttpClient, url: String, payload: String, expectedCode: Int = 200): String? {
	val responseBody = client
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

fun makePutRequest(client: HttpClient, url: String, payload: String, expectedCode: Int = 200): String? {
	val responseBody = client
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

fun makeDeleteRequest(client: HttpClient, url: String, expectedCode: Int = 200): String? {
	val responseBody = client
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

fun generateInBetweenCode(firstCode: String, secondCode: String): String {
	val firstCodeLower = firstCode.lowercase()
	val secondCodeLower = secondCode.lowercase()
	return firstCodeLower.zip(secondCodeLower).fold("") { acc, it ->
		if ( it.first == it.second || abs(it.first.toInt() - it.second.toInt()) == 1 ) acc + it.first
		else acc + ((it.first.toInt() + it.second.toInt())/2).toChar()
	}


}

suspend fun removeEntities(ids: List<String>, objectMapper: ObjectMapper?) {
	val auth = "Basic ${java.util.Base64.getEncoder().encodeToString("${System.getenv("ICURE_COUCHDB_USERNAME")}:${System.getenv("ICURE_COUCHDB_PASSWORD")}".toByteArray())}"
	val client = HttpClient.create().headers { h ->
		h.set("Authorization", auth)
		h.set("Content-type", "application/json")
	}

	ids.forEach { id ->
		client.get()
			.uri("${System.getenv("ICURE_COUCHDB_URL")}/${System.getenv("ICURE_COUCHDB_PREFIX")}-base/${URLEncoder.encode(id, Charsets.UTF_8)}")
			.responseSingle { response, buffer ->
				if (response.status().code() < 400) {
					buffer.asString(StandardCharsets.UTF_8).mapNotNull {
						objectMapper?.readValue(it, object : TypeReference<IdWithRev>() {})
					}.flatMap {
						it?.let {
							client.delete().uri("${System.getenv("ICURE_COUCHDB_URL")}/${System.getenv("ICURE_COUCHDB_PREFIX")}-base/${URLEncoder.encode(id, Charsets.UTF_8)}?rev=${URLEncoder.encode(it.rev, Charsets.UTF_8)}").response()
						} ?: Mono.empty()
					}
				} else Mono.empty()
			}.awaitFirstOrNull()
	}
}

private fun ByteArray.keyToHexString() = asUByteArray().joinToString("") { it.toString(16).padStart(2, '0') }

suspend fun createPatientUser(httpClient: HttpClient,
	apiUrl: String,
	groupId: String,
	passwordEncoder: PasswordEncoder): UserCredentials {
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
		passwordHash = passwordHash,
		patientId = patientToCreate.id
	)

	makePostRequest(httpClient, "$apiUrl/rest/v1/patient", objectMapper.writeValueAsString(patientToCreate))
	makePostRequest(httpClient, "$apiUrl/rest/v1/user", objectMapper.writeValueAsString(userToCreate))

	return UserCredentials(
		groupId,
		userToCreate.id,
		patientToCreate.id,
		username,
		password,
		privateKey
	)
}

data class UserCredentials(
	val groupId: String,
	val userId: String,
	val dataOwnerId: String,
	val username: String,
	val password: String,
	val privateKey: String?
) {
	fun auth() = "Basic ${java.util.Base64.getEncoder().encodeToString("${username}:${password}".toByteArray())}"

}
