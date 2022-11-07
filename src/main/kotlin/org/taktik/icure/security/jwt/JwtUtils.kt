package org.taktik.icure.security.jwt

import java.security.KeyPair
import java.util.Date
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JwtUtils(
	@Value("\${icure.auth.jwt.expirationMillis}") private val expirationTimeMillis: Long,
	@Value("\${icure.auth.jwt.refreshExpirationMillis}") private val refreshExpirationTimeMillis: Long
) {

	val authKeyPair: KeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256)
	val refreshKeyPair: KeyPair = Keys.keyPairFor(SignatureAlgorithm.RS256)

	fun createJWT(details: JwtDetails): String {
		return Jwts.builder()
			.setClaims(
				mapOf(
					"principalPermissions" to details.getPermissions(),
					"userId" to details.userId,
					"dataOwnerId" to details.dataOwnerId,
					"dataOwnerType" to details.dataOwnerType,
					"authorities" to details.authorities
				)
			)
			.setExpiration(Date(System.currentTimeMillis() + expirationTimeMillis))
			.signWith(authKeyPair.private, SignatureAlgorithm.RS256)
			.compact()
	}

	fun decodeAndGetClaims(jwt: String): Claims {
		return Jwts.parserBuilder()
			.setSigningKey(authKeyPair.public)
			.build()
			.parseClaimsJws(jwt)
			.body
	}

	fun createRefreshJWT(details: JwtDetails): String {
		return Jwts.builder()
			.setClaims(
				mapOf(
					"userId" to details.userId,
					"tokenId" to details.refreshTokenId
				)
			)
			.setExpiration(Date(System.currentTimeMillis() + refreshExpirationTimeMillis))
			.signWith(refreshKeyPair.private, SignatureAlgorithm.RS256)
			.compact()
	}

	fun decodeAndGetRefreshClaims(jwt: String): Claims {
		return Jwts.parserBuilder()
			.setSigningKey(refreshKeyPair.public)
			.build()
			.parseClaimsJws(jwt)
			.body
	}

}
