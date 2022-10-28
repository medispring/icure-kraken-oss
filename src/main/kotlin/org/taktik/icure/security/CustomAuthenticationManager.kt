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

package org.taktik.icure.security

import java.net.URI
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.security.SecurityException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactor.mono
import org.apache.commons.logging.LogFactory
import org.jboss.aerogear.security.otp.Totp
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.SpringSecurityMessageSource
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.util.Assert
import org.taktik.icure.asyncdao.UserDAO
import org.taktik.icure.asynclogic.PermissionLogic
import org.taktik.icure.constants.Users
import org.taktik.icure.entities.User
import org.taktik.icure.entities.security.Permission
import org.taktik.icure.entities.security.PermissionItem
import org.taktik.icure.exceptions.Invalid2FAException
import org.taktik.icure.exceptions.InvalidJwtException
import org.taktik.icure.exceptions.Missing2FAException
import org.taktik.icure.properties.CouchDbProperties
import org.taktik.icure.security.jwt.JwtDetails
import org.taktik.icure.security.jwt.JwtRefreshDetails
import org.taktik.icure.security.jwt.JwtUtils
import reactor.core.publisher.Mono

data class GlobalClusterAccumulator(
	val matchingUsers: List<User> = listOf(),
	val totpFailedUsers: List<User> = listOf(),
	val totpMissingUsers: List<User> = listOf()
)

data class JwtAuthenticationToken(
	private val authorities: MutableSet<GrantedAuthority> = mutableSetOf(),
	private val encodedJwt: String,
	private val claims: JwtDetails,
	private val details: Map<String, Any> = mapOf(),
	private var authenticated: Boolean = false
): Authentication{
	override fun getName(): String = "jwt"
	override fun getAuthorities(): MutableCollection<out GrantedAuthority> = authorities
	override fun getCredentials(): Any = encodedJwt
	override fun getDetails(): Any = details
	override fun getPrincipal(): Any = claims
	override fun isAuthenticated(): Boolean = authenticated
	override fun setAuthenticated(isAuthenticated: Boolean) {
		authenticated = isAuthenticated
	}
}

@ExperimentalCoroutinesApi
class CustomAuthenticationManager(
	couchDbProperties: CouchDbProperties,
	private val userDAO: UserDAO, //prevent cyclic dependencies
	private val permissionLogic: PermissionLogic,
	private val passwordEncoder: PasswordEncoder,
	private val messageSourceAccessor: MessageSourceAccessor = SpringSecurityMessageSource.getAccessor(),
	private val jwtUtils: JwtUtils,
) : CustomReactiveAuthenticationManager {
	private val log = LogFactory.getLog(javaClass)

	override fun authenticate(authentication: Authentication?): Mono<Authentication> = mono {
		try {
			// TODO: the if will be removed when the SESSION will be dismissed
			if (authentication is UsernamePasswordAuthenticationToken) {
				authenticateWithUsernameAndPassword(authentication).map {
					JwtAuthenticationToken(
						claims = it.principal as JwtDetails,
						authorities = (it.authorities as Collection<Any?>)
							.fold(setOf<GrantedAuthority>()) { acc, x -> acc + (x as GrantedAuthority) }.toMutableSet(),
						encodedJwt = jwtUtils.createJWT(it.principal as JwtDetails),
						authenticated = true
					)
				}.awaitFirstOrNull()
					.also {
						loadSecurityContext()?.map { ctx ->
							ctx.authentication = it
						}?.awaitFirstOrNull()
					}
			} else {
				jwtUtils.decodeAndGetClaims(authentication?.credentials as String)
					.let {
						val authorities = (it["authorities"] as Collection<Any?>)
							.filterIsInstance<Map<String, String>>()
							.fold(setOf<GrantedAuthority>()) { acc, x -> acc + SimpleGrantedAuthority(x.values.first()) }.toMutableSet()
						JwtAuthenticationToken(
							claims = JwtDetails(
								userId = it["userId"] as String,
								dataOwnerId = it["dataOwnerId"] as String?,
								dataOwnerType = it["dataOwnerType"] as String?,
								authorities = authorities,
								principalPermissions = (it["principalPermissions"] as List<Any?>).fold(setOf()) { acc, x ->
									val permissionMap = x as LinkedHashMap<*, *>
									acc + Permission(
										(permissionMap["grants"] as List<PermissionItem>).toSet(),
										(permissionMap["revokes"] as List<PermissionItem>).toSet())
								}
							),
							encodedJwt = authentication.credentials as String,
							authorities = authorities,
							authenticated = true
						)
					}.also {
						loadSecurityContext()?.map { ctx ->
							ctx.authentication = it
						}?.awaitFirstOrNull()
					}
			}
		} catch (e: Exception) {
			val message = e.message ?: "An error occurred while decoding Jwt"
			when(e) {
				is ExpiredJwtException -> throw InvalidJwtException(message)
				is SecurityException -> throw InvalidJwtException(message)
				is MalformedJwtException -> throw InvalidJwtException(message)
				else -> throw e
			}
		}
	}

	override fun authenticateWithUsernameAndPassword(authentication: Authentication?): Mono<Authentication> = mono {
		authentication?.principal ?: throw BadCredentialsException("Invalid username or password")
		Assert.isInstanceOf(
			UsernamePasswordAuthenticationToken::class.java,
			authentication,
			messageSourceAccessor.getMessage(
				"AbstractUserDetailsAuthenticationProvider.onlySupports",
				"Only UsernamePasswordAuthenticationToken is supported"
			)
		)

		val username: String = authentication.name
		val isFullToken = username.matches(Regex("(.+/)([0-9a-zA-Z]{8}-?[0-9a-zA-Z]{4}-?[0-9a-zA-Z]{4}-?[0-9a-zA-Z]{4}-?[0-9a-zA-Z]{12}|idUser_.+)"))
		val isPartialToken = username.matches(Regex("[0-9a-zA-Z]{8}-?[0-9a-zA-Z]{4}-?[0-9a-zA-Z]{4}-?[0-9a-zA-Z]{4}-?[0-9a-zA-Z]{12}|idUser_.+"))

		val users = when {
			isFullToken -> {
				listOfNotNull(userDAO.get(username.replace(Regex("(.+/)"), "")))
			}
			isPartialToken -> {
				listOfNotNull(userDAO.get(username))
			}
			else -> {
				userDAO.listUsersByUsername(username).toList() + try {
					userDAO.listUsersByEmail(username).toList()
				} catch (e: Exception) {
					log.warn("Error while loading user by email", e)
					emptyList()
				} + try {
					userDAO.listUsersByPhone(username).toList()
				} catch (e: Exception) {
					log.warn("Error while loading user by phone", e)
					emptyList()
				}
			}
		}.filter { it.status == Users.Status.ACTIVE && it.deletionDate == null }.sortedBy { it.id }.distinctBy { it.id }

		val password: String = authentication.credentials.toString()

		val accumulatedUsers = users.fold(GlobalClusterAccumulator()) { acc, candidate ->
			when (isPasswordValid(candidate, password)) {
				PasswordValidationStatus.SUCCESS -> acc.copy(matchingUsers = acc.matchingUsers + candidate)
				PasswordValidationStatus.FAILURE -> acc.also { log.debug { "No match for $username" }}
				PasswordValidationStatus.MISSING_2FA -> acc.copy(totpMissingUsers = acc.totpMissingUsers + candidate)
				PasswordValidationStatus.FAILED_2FA -> acc.copy(totpFailedUsers = acc.totpFailedUsers + candidate)
			}
		}

		if (accumulatedUsers.totpMissingUsers.isNotEmpty()) {
			throw Missing2FAException("Missing verification code")
		}

		if (accumulatedUsers.totpFailedUsers.isNotEmpty()) {
			throw Invalid2FAException("Invalid verification code")
		}

		val user = accumulatedUsers.matchingUsers.firstOrNull()

		if (user == null) {
			log.warn("Invalid username or password for user " + username + ", no user matched out of " + users.size + " candidates")
			throw BadCredentialsException("Invalid username or password")
		}

		// Build permissionSetIdentifier
		val authorities = getAuthorities(user)
		val userDetails = JwtDetails(
			authorities = authorities,
			principalPermissions = user.permissions,
			userId = user.id,
			dataOwnerId = user.healthcarePartyId ?: user.patientId ?: user.deviceId,
			dataOwnerType = if (user.healthcarePartyId != null) JwtDetails.DATA_OWNER_HCP
			else if (user.patientId != null) JwtDetails.DATA_OWNER_PATIENT
			else if (user.deviceId != null) JwtDetails.DATA_OWNER_DEVICE
			else null
		)

		UsernamePasswordAuthenticationToken(
			userDetails,
			authentication,
			authorities
		)
	}

	override suspend fun regenerateJwtDetails(jwtRefreshDetails: JwtRefreshDetails): JwtDetails {
		val user = userDAO.findUserOnUserDb(jwtRefreshDetails.userId, false)
		return user?.let {
			if (it.status != Users.Status.ACTIVE || it.deletionDate != null) throw InvalidJwtException("Cannot create access token for non-active user")
			JwtDetails(
				authorities = getAuthorities(user),
				principalPermissions = it.permissions,
				userId = jwtRefreshDetails.userId,
				dataOwnerId = it.healthcarePartyId
					?: it.patientId
					?: it.deviceId,
				dataOwnerType = if (it.healthcarePartyId != null) JwtDetails.DATA_OWNER_HCP
				else if (it.patientId != null) JwtDetails.DATA_OWNER_PATIENT
				else if (it.deviceId != null) JwtDetails.DATA_OWNER_DEVICE
				else null,
				groupIdUserIdMatching = jwtRefreshDetails.groupIdUserIdMatching
			)
		} ?: throw InvalidJwtException("Cannot refresh authentication token for this user")
	}

	private suspend fun getAuthorities(user: User): Set<GrantedAuthority> {
		val permissionSetIdentifier = PermissionSetIdentifier(User::class.java, user.id)
		return permissionLogic.getPermissionSet(permissionSetIdentifier, user)?.grantedAuthorities ?: setOf()
	}


	private enum class PasswordValidationStatus {
		SUCCESS,
		FAILURE,
		MISSING_2FA,
		FAILED_2FA;

		companion object {
			fun PasswordValidationStatus.isSuccess() = this == SUCCESS
		}
	}

	/**
	 * Checks if a password is valid, the password can contain the verification code of the 2FA following this format `password|123456`.
	 *
	 * @returns [PasswordValidationStatus] depending of the status of the validation:
	 * - [PasswordValidationStatus.SUCCESS]: Everything is checked and good
	 * - [PasswordValidationStatus.FAILURE]: Password isn't correct
	 * - [PasswordValidationStatus.MISSING_2FA]: Password validated, but 2FA verification code is missing
	 * - [PasswordValidationStatus.FAILED_2FA]: Password validated, but 2FA verification code is wrong
	 */
	private fun isPasswordValid(u: User, password: String): PasswordValidationStatus {
		if (doesUserContainsToken(u, appToken = password)) {
			return PasswordValidationStatus.SUCCESS
		}
		return when {
			u.passwordHash == null -> PasswordValidationStatus.FAILURE
			u.use2fa == true && u.secret?.isNotBlank() == true -> {
				if (passwordEncoder.matches(strip2fa(password).takeIf { it.isNotEmpty() } ?: password, u.passwordHash)) {
					val verificationCode = password.split("|").takeIf { it.size >= 2 }?.last()
					if (verificationCode != null && isValidLong(verificationCode)) {
						val totp = Totp(u.secret)
						if (totp.verify(verificationCode)) {
							return PasswordValidationStatus.SUCCESS
						}
						return PasswordValidationStatus.FAILED_2FA
					}
					return PasswordValidationStatus.MISSING_2FA
				}
				return PasswordValidationStatus.FAILURE
			}
			else -> {
				passwordEncoder
					.matches(password, u.passwordHash).takeIf { it }
					?.let { PasswordValidationStatus.SUCCESS }
					?: PasswordValidationStatus.FAILURE
			}
		}
	}

	private fun strip2fa(password: String) = password
		.split("|").toTypedArray().toList()
		.dropLast(1)
		.joinToString("|")

	private fun applicationsContainingToken(u: User, appToken: String): List<String> {
		return (
			(u.applicationTokens ?: emptyMap())
				.filterValues { it == appToken } +
				u.authenticationTokens
					.filter { (_, authToken) -> !authToken.isExpired() }
					.filterValues { it.token == appToken }
			)
			.map { (application, _) -> application }
	}

	private fun doesUserContainsToken(u: User, appToken: String) = u.applicationTokens?.containsValue(appToken) == true ||
		u.authenticationTokens
			.filter { (_, authToken) -> !authToken.isExpired() }
			.map { (_, authToken) -> authToken.token }
			.any { token -> passwordEncoder.matches(appToken, token) }

	private fun isValidLong(code: String): Boolean {
		try {
			code.toLong()
		} catch (e: NumberFormatException) {
			return false
		}
		return true
	}
}
