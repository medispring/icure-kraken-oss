package org.taktik.icure.security.jwt

data class JwtRefreshDetails (
	val userId: String,
	val groupIdUserIdMatching: List<String> = emptyList()
)
