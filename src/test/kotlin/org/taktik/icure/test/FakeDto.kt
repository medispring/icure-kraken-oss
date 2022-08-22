package org.taktik.icure.test

import org.taktik.couchdb.entity.Versionable

data class FakeDto(override val rev: String?, override val revHistory: Map<String, String>?, override val id: String) : Versionable<String> {
	override fun withIdRev(id: String?, rev: String): Versionable<String> {
		TODO("Not yet implemented")
	}
}
