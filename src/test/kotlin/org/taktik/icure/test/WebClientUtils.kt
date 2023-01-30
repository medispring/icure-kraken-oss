package org.taktik.icure.test

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.apache.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.util.UriUtils

val authorizationString by lazy {
	"Basic ${
		java.util.Base64.getEncoder()
			.encodeToString("${ICureTestApplication.masterHcp.userId}:${ICureTestApplication.masterHcp.password}".toByteArray())
	}"
}

inline fun shouldRespondErrorStatus(status: HttpStatus, block: () -> Unit) {
	shouldThrow<WebClientResponseException> {
		block()
	}.statusCode shouldBe status
}

private fun encodedQueryParameter(key: String, value: Any?): String? =
	if (value != null) when (value) {
		is Collection<*> -> value.mapNotNull { encodedQueryParameter(key, it) }.joinToString("&")
		else -> "$key=${UriUtils.encode(value.toString(), "UTF-8")}"
	} else null

fun uriWithVars(uri: String, vars: Map<String, Any?>) =
	vars.mapNotNull { encodedQueryParameter(it.key, it.value) }.let { encodedQueryParameters ->
		if (encodedQueryParameters.isEmpty())
			uri
		else
			encodedQueryParameters.toList().joinToString("&").let { "$uri?$it" }
	}

fun <S : WebClient.RequestHeadersSpec<*>> WebClient.UriSpec<S>.uriWithVars(uri: String, vars: Map<String, Any?>) =
	uri(org.taktik.icure.test.uriWithVars(uri, vars))

fun <T : WebClient.RequestHeadersSpec<T>> WebClient.RequestHeadersSpec<T>.jsonContent() =
	header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)

fun <T : WebClient.RequestHeadersSpec<T>> WebClient.RequestHeadersSpec<T>.bytesContent() =
	header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)

fun <T : WebClient.RequestHeadersSpec<T>> WebClient.RequestHeadersSpec<T>.multipartContent() =
	header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA_VALUE)
