package org.taktik.icure.security

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.core.Authentication
import org.taktik.icure.security.jwt.JwtDetails
import org.taktik.icure.security.jwt.JwtRefreshDetails
import reactor.core.publisher.Mono

interface CustomReactiveAuthenticationManager: ReactiveAuthenticationManager {

	fun authenticateWithUsernameAndPassword(authentication: Authentication?): Mono<Authentication>

	suspend fun regenerateJwtDetails(jwtRefreshDetails: JwtRefreshDetails): JwtDetails
}
