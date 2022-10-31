package org.taktik.icure.security.jwt

data class JwtRefreshDetails (
	val userId: String,
	val tokenId: String
)
