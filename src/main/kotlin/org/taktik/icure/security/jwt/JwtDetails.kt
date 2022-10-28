package org.taktik.icure.security.jwt

import org.springframework.security.core.GrantedAuthority
import org.taktik.icure.entities.security.Permission
import org.taktik.icure.security.AbstractUserDetails

class JwtDetails(
	authorities: Set<GrantedAuthority>,
	principalPermissions: Set<Permission>,
	userId: String,
	val refreshTokenId: String? = null,
	val dataOwnerId: String?,
	val dataOwnerType: String?,
	val groupIdUserIdMatching: List<String> = emptyList()
): AbstractUserDetails(
	userId, authorities, principalPermissions
) {
	companion object {
		const val DATA_OWNER_HCP = "hcp"
		const val DATA_OWNER_PATIENT = "patient"
		const val DATA_OWNER_DEVICE = "device"
	}
}
