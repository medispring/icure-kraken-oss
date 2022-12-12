package org.taktik.icure.test.fake

import java.net.URI
import java.util.stream.Stream
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.reactor.ReactorContext
import kotlinx.coroutines.withContext
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContext
import org.taktik.icure.asyncdao.UserDAO
import org.taktik.icure.asynclogic.AsyncSessionLogic
import org.taktik.icure.asynclogic.impl.AsyncSessionLogicImpl
import org.taktik.icure.constants.Roles
import org.taktik.icure.entities.User
import org.taktik.icure.properties.CouchDbProperties
import org.taktik.icure.security.AuthorizationService
import org.taktik.icure.security.PermissionSetIdentifier
import org.taktik.icure.security.jwt.JwtDetails
import org.taktik.icure.test.newId
import reactor.core.publisher.Mono
import reactor.util.context.Context

/**
 * Utilities to mock a session.
 */
@OptIn(ExperimentalCoroutinesApi::class)
class SessionMock {
	private val userDaoMock = mockk<UserDAO>()
	val sessionLogic: AsyncSessionLogic = AsyncSessionLogicImpl(mockk(), userDaoMock, CouchDbProperties())
	val authorizationService = AuthorizationService(sessionLogic, mockk())

	@OptIn(ExperimentalCoroutinesApi::class)
	private suspend fun <T> withReactorContext(authentication: Authentication, block: suspend () -> T): T {
		val context = mockk<Context>()
		val securityContext = mockk<SecurityContext>()
		every { context.isEmpty } returns false
		every { context.size() } returns 1
		every { context.stream() } answers { Stream.of(mapOf(SecurityContext::class.java as Any to securityContext).entries.first()) }
		every { context.get<Mono<SecurityContext>>(SecurityContext::class.java) } answers { Mono.just(securityContext) }
		every { securityContext.authentication } returns authentication
		return withContext(currentCoroutineContext() + ReactorContext(context)) { block() }
	}

	/**
	 * Act as an authenticated user, with a specific group id, but without any specific permission or uri.
	 */
	suspend fun <T> withGroupContext(groupId: String, block: suspend () -> T): T {
		val authentication = mockk<Authentication>()
		val dbUser = mockk<JwtDetails>()
		every { authentication.principal } returns dbUser
		every { dbUser.getPermissions() } returns mockk()
		return withReactorContext(authentication, block)
	}

	/**
	 * Act as an authenticated user, with a specific uri and group id, but without any specific permission.
	 */
	suspend fun <T> withAuthenticatedContext(uri: URI, groupId: String, block: suspend () -> T): T {
		val authentication = mockk<Authentication>()
		val dbUser = mockk<JwtDetails>()
		every { authentication.principal } returns dbUser
		return withReactorContext(authentication) { block() }
	}

	suspend fun <T> withAuthoritiesContext(uri: URI, groupId: String, authorities: Collection<GrantedAuthority>, block: suspend () -> T): T {
		val authentication = mockk<Authentication>()
		val dbUser = mockk<JwtDetails>()
		every { authentication.principal } returns dbUser
		every { dbUser.authorities } returns authorities
		return withReactorContext(authentication) { block() }
	}

	suspend fun <T> withAuthenticatedHcpContext(
		uri: URI,
		groupId: String,
		hcpId: String, block: suspend () -> T
	): T {
		val user = User(
			newId(),
			healthcarePartyId = hcpId
		)
		return withAuthenticatedUserContext(uri, groupId, Roles.GrantedAuthority.ROLE_HCP, user, block)
	}

	suspend fun <T> withAuthenticatedPatientContext(
		uri: URI,
		groupId: String,
		patientId: String,
		block: suspend () -> T
	): T {
		val user = User(
			newId(),
			patientId = patientId
		)
		return withAuthenticatedUserContext(uri, groupId, Roles.GrantedAuthority.ROLE_PATIENT, user, block)
	}

	private suspend fun <T> withAuthenticatedUserContext(
		uri: URI,
		groupId: String,
		role: String,
		user: User,
		block: suspend () -> T
	): T {
		val authentication = mockk<Authentication>()
		val dbUser = mockk<JwtDetails>()
		val grantedAuthority = mockk<GrantedAuthority>()
		val permissionSetIdentifier = mockk<PermissionSetIdentifier>()
		every { grantedAuthority.authority } returns role
		every { authentication.principal } returns dbUser
		every { dbUser.authorities } returns listOf(grantedAuthority)
		every { permissionSetIdentifier.getPrincipalIdOfClass(User::class.java) } returns user.id
		coEvery { userDaoMock.getUserOnUserDb(user.id , any()) } returns user
		return withReactorContext(authentication) { block() }
	}
}
