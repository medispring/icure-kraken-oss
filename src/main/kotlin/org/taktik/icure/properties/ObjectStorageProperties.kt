package org.taktik.icure.properties

import java.io.File
import java.lang.IllegalArgumentException
import java.net.URI
import java.net.URISyntaxException
import org.springframework.beans.factory.InitializingBean
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties("icure.objectstorage")
final data class ObjectStorageProperties(
	/**
	 * Path to the directory to use as local cache and temporary storage for object storage attachments which needs to be uploaded.
	 */
    var cacheLocation: String = "",
	/**
	 * Url to the icure cloud version, for upload to the object storage service.
	 */
    var icureCloudUrl: String = "",
	/**
	 * If true enables automatic migration of existing attachments to object storage if they are above the size limit.
	 */
    var backlogToObjectStorage: Boolean = true,
	/**
	 * Minimum size for new attachments to be stored to object storage (in bytes).
	 */
    var sizeLimit: Long = 2_000_000,
	/**
	 * Minimum size for existing attachments to be migrated to object storage (in bytes).
	 * Must be greater than or equal to [sizeLimit].
	 */
	var migrationSizeLimit: Long = 4_000_000,
	/**
	 * Delay in milliseconds between when a migratable attachment is found and when the migration is actually executed.
	 */
	var migrationDelayMs: Long = 15 * 60 * 1000,
) : InitializingBean {
	override fun afterPropertiesSet() {
		require(migrationSizeLimit >= sizeLimit) {
			"Migration size limit must be greater than or equal to sizeLimit"
		}
		try {
			require(icureCloudUrl.isNotBlank()) { "icureCloudUrl can't be blank" }
			URI(icureCloudUrl)
		} catch (e: URISyntaxException) {
			throw IllegalArgumentException("icureCloudUrl=\"$icureCloudUrl\" must be a valid uri", e)
		}
		require(cacheLocation.isNotBlank() && File(cacheLocation).also { it.mkdirs() }.let { it.isDirectory && it.canRead() && it.canWrite() }) {
			"cacheLocation=\"$cacheLocation\" must be a folder where the icure application has read/write access"
		}
	}
}
