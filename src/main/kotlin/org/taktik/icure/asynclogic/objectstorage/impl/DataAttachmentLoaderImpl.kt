package org.taktik.icure.asynclogic.objectstorage.impl

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.stereotype.Service
import org.taktik.couchdb.entity.Attachment
import org.taktik.couchdb.id.Identifiable
import org.taktik.icure.asyncdao.DocumentDAO
import org.taktik.icure.asyncdao.GenericDAO
import org.taktik.icure.asynclogic.objectstorage.DataAttachmentLoader
import org.taktik.icure.asynclogic.objectstorage.DocumentDataAttachmentLoader
import org.taktik.icure.asynclogic.objectstorage.DocumentObjectStorage
import org.taktik.icure.asynclogic.objectstorage.DocumentObjectStorageMigration
import org.taktik.icure.asynclogic.objectstorage.IcureObjectStorage
import org.taktik.icure.asynclogic.objectstorage.IcureObjectStorageMigration
import org.taktik.icure.entities.Document
import org.taktik.icure.entities.base.HasDataAttachments
import org.taktik.icure.entities.embed.DataAttachment
import org.taktik.icure.properties.ObjectStorageProperties
import org.taktik.icure.utils.toByteArray

class DataAttachmentLoaderImpl<T : HasDataAttachments>(
	private val dao: GenericDAO<T>,
	private val icureObjectStorage: IcureObjectStorage<T>,
	private val icureObjectStorageMigration: IcureObjectStorageMigration<T>,
	private val objectStorageProperties: ObjectStorageProperties
): DataAttachmentLoader<T> {
	override fun contentFlowOf(
		target: T,
		retrieveAttachment: T.() -> DataAttachment
	): Flow<DataBuffer> = target.retrieveAttachment().let { attachment ->
		attachment.contentFlowFromCacheOrLoad { doLoadFlow(target, attachment) }
	}

	override suspend fun contentBytesOf(
		target: T,
		retrieveAttachment: T.() -> DataAttachment
	): ByteArray = target.retrieveAttachment().let { attachment ->
		attachment.contentBytesFromCacheOrLoad { doLoadFlow(target, attachment) }
	}

	private fun doLoadFlow(target: T, attachment: DataAttachment): Flow<DataBuffer> =
		attachment.objectStoreAttachmentId?.let {
			icureObjectStorage.readAttachment(target, it)
		} ?: attachment.couchDbAttachmentId!!.let { attachmentId ->
			if (icureObjectStorageMigration.isMigrating(target, attachmentId)) {
				icureObjectStorage.tryReadCachedAttachment(target, attachmentId) ?: loadCouchDbAttachment(target, attachmentId)
			} else if (shouldMigrate(target, attachmentId)) {
				flow {
					if (tryPreMigrate(target, attachmentId)) {
						icureObjectStorageMigration.scheduleMigrateAttachment(target, attachmentId)
					}
					emitAll(icureObjectStorage.tryReadCachedAttachment(target, attachmentId) ?: loadCouchDbAttachment(target, attachmentId))
				}
			} else {
				loadCouchDbAttachment(target, attachmentId)
			}
		}

	private fun loadCouchDbAttachment(target: T, attachmentId: String) =
		dao.getAttachment(target.id, attachmentId).map { DefaultDataBufferFactory.sharedInstance.wrap(it) }

	private fun shouldMigrate(target: T, attachmentId: String) =
		objectStorageProperties.backlogToObjectStorage
			&& target.attachments?.get(attachmentId)?.let { it.contentLength >= objectStorageProperties.sizeLimit } == true

	private suspend fun tryPreMigrate(target: T, attachmentId: String) =
		!icureObjectStorageMigration.isMigrating(target, attachmentId)
			&& icureObjectStorageMigration.preMigrate(target, attachmentId, loadCouchDbAttachment(target, attachmentId))
}

@Service
class DocumentDataAttachmentLoaderImpl(
	dao: DocumentDAO,
	objectStorage: DocumentObjectStorage,
	objectStorageMigration: DocumentObjectStorageMigration,
	objectStorageProperties: ObjectStorageProperties
) : DocumentDataAttachmentLoader, DataAttachmentLoader<Document> by DataAttachmentLoaderImpl(
	dao,
	objectStorage,
	objectStorageMigration,
	objectStorageProperties
)
