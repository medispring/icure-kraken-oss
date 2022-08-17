/*
 * Copyright (c) 2020. Taktik SA, All rights reserved.
 */
package org.taktik.icure.security

import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.ServerAuthenticationEntryPoint
import org.springframework.web.server.ServerWebExchange
import org.taktik.icure.exceptions.Invalid2FAException
import org.taktik.icure.exceptions.Missing2FAException
import reactor.core.publisher.Mono

class UnauthorizedEntryPoint() : ServerAuthenticationEntryPoint {
	override fun commence(exchange: ServerWebExchange, e: AuthenticationException): Mono<Void> {
		exchange.response.statusCode = when (e) {
			is Missing2FAException -> HttpStatus.EXPECTATION_FAILED
			is Invalid2FAException -> HttpStatus.NOT_ACCEPTABLE
			else -> HttpStatus.UNAUTHORIZED
		}
		return Mono.empty()
	}
}
