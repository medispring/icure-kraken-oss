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

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.mono
import org.apache.commons.logging.LogFactory
import org.jboss.aerogear.security.otp.Totp
import org.springframework.context.support.MessageSourceAccessor
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.SpringSecurityMessageSource
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.util.Assert
import org.taktik.icure.asyncdao.UserDAO
import org.taktik.icure.asynclogic.PermissionLogic
import org.taktik.icure.constants.Users
import org.taktik.icure.entities.User
import org.taktik.icure.exceptions.Invalid2FAException
import org.taktik.icure.exceptions.Missing2FAException
import org.taktik.icure.properties.CouchDbProperties
import org.taktik.icure.security.database.DatabaseUserDetails
import reactor.core.publisher.Mono

@ExperimentalCoroutinesApi
class CustomAuthenticationManager(
	couchDbProperties: CouchDbProperties,
	private val userDAO: UserDAO, //prevent cyclic dependnecies
	private val permissionLogic: PermissionLogic,
	private val passwordEncoder: PasswordEncoder,
	private val messageSourceAccessor: MessageSourceAccessor = SpringSecurityMessageSource.getAccessor()
) : ReactiveAuthenticationManager {
	private val log = LogFactory.getLog(javaClass)

	override fun authenticate(authentication: Authentication?): Mono<Authentication> = mono {
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

		val matchingUsers = mutableListOf<User>()
		val totpFailedUsers = mutableListOf<User>()
		val totpMissingUsers = mutableListOf<User>()
		val password: String = authentication.credentials.toString()

		for (candidate in users) {
			when (isPasswordValid(candidate, password)) {
				PasswordValidationStatus.SUCCESS -> {
					matchingUsers.add(candidate)
				}
				PasswordValidationStatus.FAILURE -> log.debug { "No match for $username" }
				PasswordValidationStatus.MISSING_2FA -> totpMissingUsers.add(candidate)
				PasswordValidationStatus.FAILED_2FA -> totpFailedUsers.add(candidate)
			}
		}

		val user: User? = matchingUsers.firstOrNull()

		if (totpMissingUsers.isNotEmpty()) {
			throw Missing2FAException("Missing verification code")
		}

		if (totpFailedUsers.isNotEmpty()) {
			throw Invalid2FAException("Invalid verification code")
		}

		if (user == null) {
			log.warn("Invalid username or password for user " + username + ", no user matched out of " + users.size + " candidates")
			throw BadCredentialsException("Invalid username or password")
		}

		// Build permissionSetIdentifier
		val permissionSetIdentifier = PermissionSetIdentifier(User::class.java, user.id)
		val authorities = permissionLogic.getPermissionSet(permissionSetIdentifier, user)?.grantedAuthorities ?: setOf()
		val userDetails = DatabaseUserDetails(
			permissionSetIdentifier = permissionSetIdentifier,
			authorities = authorities,
			principalPermissions = user.permissions,
			passwordHash = user.passwordHash,
			secret = user.secret,
			use2fa = user.use2fa ?: false,
			rev = user.rev,
			applicationTokens = user.applicationTokens ?: emptyMap(),
			authenticationTokens = user.authenticationTokens,
			application = applicationsContainingToken(user, authentication.credentials.toString()).firstOrNull(),
			groupIdUserIdMatching = matchingUsers.map { it.id }
		)

		UsernamePasswordAuthenticationToken(
			userDetails,
			authentication,
			authorities
		)
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
