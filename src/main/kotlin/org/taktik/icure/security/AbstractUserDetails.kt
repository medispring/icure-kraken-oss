/*
 * Copyright (c) 2020. Taktik SA, All rights reserved.
 */
package org.taktik.icure.security

import org.springframework.security.core.GrantedAuthority
import org.taktik.icure.entities.security.Permission

abstract class AbstractUserDetails(
	val userId: String,
	authorities: Set<GrantedAuthority>,
	private var principalPermissions: Set<Permission>
) : UserDetails {
	private val authoritiesSet: Set<GrantedAuthority> = authorities

	fun getPermissions(): Set<Permission> = principalPermissions

	override fun getAuthorities(): Collection<GrantedAuthority> = authoritiesSet

	override var isRealAuth = true
	override var locale: String? = null
	override var logoutURL: String? = null
	override fun getUsername(): String = userId

	override fun getPassword(): String? {
		return null
	}

	override fun isAccountNonExpired(): Boolean {
		return true
	}

	override fun isAccountNonLocked(): Boolean {
		return true
	}

	override fun isCredentialsNonExpired(): Boolean {
		return true
	}

	override fun isEnabled(): Boolean {
		return true
	}

	override fun equals(o: Any?): Boolean {
		if (this === o) return true
		if (o == null || javaClass != o.javaClass) return false
		val that = o as AbstractUserDetails
		return authorities == that.authorities
	}

	override fun hashCode(): Int {
		return authorities.hashCode()
	}

	companion object {
		private const val serialVersionUID = 1L
	}
}
