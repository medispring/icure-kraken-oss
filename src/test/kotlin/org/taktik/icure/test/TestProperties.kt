package org.taktik.icure.test

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("app")
@ConfigurationProperties("icure.test")
data class TestProperties(
	var adminGroupId: String? = "xx",
	var adminGroupPassword: String? = null,
	var adminUsername: String? = "john",
	var adminPassword: String? = null,
	var groupId: String? = null,
	var groupUsername: String? = "johnny@forever.fr",
	var groupPassword: String? = null,
	var bootstrapEnv: Boolean? = false,

	var couchdb: CouchDbProperties = CouchDbProperties(),
)

class CouchDbProperties(
	var databaseName: String? = null,
	var username: String? = null,
	var password: String? = null,
)
