package org.taktik.icure.test

import java.net.URLEncoder
import java.nio.charset.StandardCharsets
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
	return HttpClient.create().headers { h ->
		h.set("Authorization", auth) //
		h.set("Content-type", "application/json")
		additionalHeaders.forEach {
			h.set(it.key, it.value)
		}
	}
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

