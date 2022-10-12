package org.taktik.icure.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.taktik.icure.asynclogic.AsyncSessionLogic
import org.taktik.icure.asynclogic.objectstorage.DocumentObjectStorageClient
import org.taktik.icure.asynclogic.objectstorage.impl.DocumentObjectStorageClientImpl
import org.taktik.icure.asynclogic.utils.CloudAuthenticationLogic
import org.taktik.icure.properties.ObjectStorageProperties

@Configuration
class ObjectStorageConfig {
	@Bean
	fun documentObjectStorageClient(
		properties: ObjectStorageProperties,
		cloudAuthenticationLogic: CloudAuthenticationLogic
	): DocumentObjectStorageClient =
		DocumentObjectStorageClientImpl(properties, cloudAuthenticationLogic)
}
