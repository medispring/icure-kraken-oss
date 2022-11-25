/*
 *  iCure Data Stack. Copyright (c) 2020 Taktik SA
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful, but
 *     WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public
 *     License along with this program.  If not, see
 *     <https://www.gnu.org/licenses/>.
 */

package org.taktik.icure.services.external.rest.v1.controllers.support

import java.util.UUID
import kotlin.coroutines.CoroutineContext
import com.fasterxml.jackson.databind.ObjectMapper
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SecurityException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.ReactorContext
import kotlinx.coroutines.reactor.asCoroutineContext
import kotlinx.coroutines.reactor.mono
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.http.server.reactive.ServerHttpResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.WebSession
import org.taktik.icure.asynclogic.AsyncSessionLogic
import org.taktik.icure.exceptions.Invalid2FAException
import org.taktik.icure.exceptions.InvalidJwtException
import org.taktik.icure.exceptions.Missing2FAException
import org.taktik.icure.security.SecurityToken
import org.taktik.icure.security.jwt.JwtDetails
import org.taktik.icure.security.jwt.JwtRefreshDetails
import org.taktik.icure.security.jwt.JwtResponse
import org.taktik.icure.security.jwt.JwtUtils
import org.taktik.icure.services.external.rest.v1.dto.AuthenticationResponse
import org.taktik.icure.services.external.rest.v1.dto.LoginCredentials
import org.taktik.icure.spring.asynccache.AsyncCacheManager
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@ExperimentalCoroutinesApi
@RestController
@RequestMapping("/rest/v1/auth")
@Tag(name = "auth")
class LoginController(
	private val sessionLogic: AsyncSessionLogic,
	private val jwtUtils: JwtUtils,
	private val objectMapper: ObjectMapper,
	asyncCacheManager: AsyncCacheManager
) {
	val cache = asyncCacheManager.getCache<String, SecurityToken>("spring.security.tokens")
	val refreshJwtCache = asyncCacheManager.getCache<String, Boolean>("org.taktik.icure.security.RefreshJWT")

	@Value("\${spring.session.enabled}")
	private val sessionEnabled: Boolean = false

	@Operation(summary = "login", description = "Login using username and password")
	@PostMapping("/login")
	fun login(request: ServerHttpRequest, @RequestBody loginCredentials: LoginCredentials, @Parameter(hidden = true) session: WebSession) = mono {
		try {
			val authentication = sessionLogic.login(loginCredentials.username!!, loginCredentials.password!!, request, if (sessionEnabled) session else null)
			if (authentication != null && authentication.isAuthenticated && sessionEnabled) {
				val secContext = SecurityContextImpl(authentication)
				val securityContext = kotlin.coroutines.coroutineContext[ReactorContext]?.context?.put(SecurityContext::class.java, Mono.just(secContext))
				withContext(kotlin.coroutines.coroutineContext.plus(securityContext?.asCoroutineContext() as CoroutineContext)) {
					ResponseEntity.ok().body(
						AuthenticationResponse(
							successful = true,
							token = jwtUtils.createJWT(authentication.principal as JwtDetails),
							refreshToken = jwtUtils.createRefreshJWT(authentication.principal as JwtDetails),
							healthcarePartyId = (authentication.principal as JwtDetails).dataOwnerId.takeIf { (authentication.principal as JwtDetails).dataOwnerType == JwtDetails.DATA_OWNER_HCP },
							username = loginCredentials.username
						).also { session.attributes["SPRING_SECURITY_CONTEXT"] = secContext }
					)
				}
			} else if (authentication != null && authentication.isAuthenticated && !sessionEnabled) {
				ResponseEntity.ok().body(
					AuthenticationResponse(
						successful = true,
						token = jwtUtils.createJWT(authentication.principal as JwtDetails),
						refreshToken = jwtUtils.createRefreshJWT(authentication.principal as JwtDetails),
						healthcarePartyId = (authentication.principal as JwtDetails).dataOwnerId.takeIf { (authentication.principal as JwtDetails).dataOwnerType == JwtDetails.DATA_OWNER_HCP },
						username = loginCredentials.username
					)
				)
			} else {
				ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(JwtResponse(successful = false))
			}
		} catch (ex: AuthenticationException) {
			ResponseEntity.status(
				when(ex){
					is Missing2FAException -> HttpStatus.EXPECTATION_FAILED
					is Invalid2FAException -> HttpStatus.NOT_ACCEPTABLE
					is BadCredentialsException -> HttpStatus.UNAUTHORIZED
					else -> HttpStatus.UNAUTHORIZED
				}
			).body(JwtResponse(successful = false))
		}
	}

	@Operation(summary = "refresh", description = "Get a new authentication token using a refresh token")
	@PostMapping("/refresh")
	fun refresh(request: ServerHttpRequest, response: ServerHttpResponse) = mono {
		try {
			val refreshDetails = request.headers["Refresh-Token"]
				?.filterNotNull()
				?.first()
				?.replace("Bearer ", "")
				?.let { rawToken ->
					jwtUtils.decodeAndGetRefreshClaims(rawToken).let {
						JwtRefreshDetails(
							userId = it["userId"] as String,
							tokenId = it["tokenId"] as String
						)
					}
				}
				?: throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token")
			val newJwtDetails = sessionLogic.refreshToken(refreshDetails)
			JwtResponse(
				successful = true,
				token = jwtUtils.createJWT(newJwtDetails)
			)
		} catch (e: Exception) {
			response.statusCode = when(e) {
				is ExpiredJwtException -> HttpStatus.UNAUTHORIZED
				is SecurityException -> HttpStatus.UNAUTHORIZED
				is MalformedJwtException -> HttpStatus.UNAUTHORIZED
				is InvalidJwtException -> HttpStatus.UNAUTHORIZED
				else -> HttpStatus.INTERNAL_SERVER_ERROR
			}
			val body = objectMapper.writeValueAsString(mapOf("message" to e.message)).toByteArray()
			val buffer = response.bufferFactory().wrap(body)
			response.writeWith(Flux.just(buffer)).awaitFirstOrNull()
		}
	}

	@Operation(summary = "invalidate", description = "Invalidates the refresh JWT for a user")
	@PostMapping("/invalidate")
	fun invalidateRefreshJWT(request: ServerHttpRequest, response: ServerHttpResponse) = mono {
		try {
			val claims = request.headers["Refresh-Token"]
				?.filterNotNull()
				?.first()
				?.replace("Bearer ", "")
				?.let {
					jwtUtils.decodeAndGetRefreshClaims(it)
				}
				?: throw InvalidJwtException("Invalid refresh token")
			val userId = claims["userId"] as String
			val tokenId = claims["tokenId"] as String
			if (refreshJwtCache.get("$userId:$tokenId") != true) throw throw InvalidJwtException("Invalid refresh token")
			refreshJwtCache.evict("$userId:$tokenId")
			response.statusCode = HttpStatus.OK
			response.writeWith(Flux.just()).awaitFirstOrNull()
		} catch (e: Exception) {
			response.statusCode = when(e) {
				is ExpiredJwtException -> HttpStatus.UNAUTHORIZED
				is SecurityException -> HttpStatus.UNAUTHORIZED
				is MalformedJwtException -> HttpStatus.UNAUTHORIZED
				is InvalidJwtException -> HttpStatus.UNAUTHORIZED
				else -> HttpStatus.INTERNAL_SERVER_ERROR
			}
			val body = objectMapper.writeValueAsString(mapOf("message" to e.message)).toByteArray()
			val buffer = response.bufferFactory().wrap(body)
			response.writeWith(Flux.just(buffer)).awaitFirstOrNull()
		}
	}

	@Operation(summary = "logout", description = "Logout")
	@GetMapping("/logout")
	fun logout() = mono {
		sessionLogic.logout()
		AuthenticationResponse(successful = true)
	}

	@Operation(summary = "logout", description = "Logout")
	@PostMapping("/logout")
	fun logoutPost() = mono {
		sessionLogic.logout()
		AuthenticationResponse(successful = true)
	}

	@Operation(summary = "token", description = "Get token for subsequent operation")
	@GetMapping("/token/{method}/{path}")
	fun token(@PathVariable method: String, @PathVariable path: String) = mono {
		val token = UUID.randomUUID().toString()
		cache.put(token, SecurityToken(HttpMethod.valueOf(method), path, sessionLogic.getCurrentSessionContext().getAuthentication()))
		token
	}
}
