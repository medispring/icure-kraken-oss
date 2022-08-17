package org.taktik.icure.asynclogic.utils

/**
 * Logic for authentication from the current instance of icure-oss to icure-cloud
 */
interface CloudAuthenticationLogic {
	/**
	 * Get the value of an authorization header for authentication to the icure cloud as the user with the provided id.
	 * The header will only be valid if the
	 * @param userId id of the user you want to authenticate as.
	 * @return the authorization header value for the user.
	 */
	suspend fun userAuthenticationHeader(userId: String): String
}
