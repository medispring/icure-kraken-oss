package org.taktik.icure.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

/**
 * Properties for external services such as icure-cloud for object storage.
 */
@Component
@Profile("app")
@ConfigurationProperties("icure.externalservices")
data class ExternalServicesProperties(
	/**
	 * If true uses fake in-memory implementations of external services instead of doing requests to real services.
	 * This simplifies the creation of isolated test environments.
	 */
	var useFakes: Boolean = false
)
