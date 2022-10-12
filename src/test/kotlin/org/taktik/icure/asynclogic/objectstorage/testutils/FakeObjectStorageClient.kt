package org.taktik.icure.asynclogic.objectstorage.testutils

import io.kotest.matchers.collections.shouldBeIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.taktik.icure.asynclogic.objectstorage.ObjectStorageClient
import org.taktik.icure.entities.base.HasDataAttachments
import org.taktik.icure.asynclogic.objectstorage.ObjectStorageException
import org.taktik.icure.utils.toByteArray

class FakeObjectStorageClient<T : HasDataAttachments<T>>(
	override val entityGroupName: String,
	userIdsProvider: () -> Set<String>
) : ObjectStorageClient<T> {
	constructor(entityGroupName: String, userIds: Set<String>) : this(entityGroupName, { userIds })

	var available = true

	val eventsChannel = Channel<ObjectStoreEvent>(UNLIMITED)

	private val entityToAttachments = mutableMapOf<String, MutableMap<String, ByteArray>>()
	private val userIds by lazy(userIdsProvider)

	val attachmentsKeys get() = entityToAttachments.flatMap { (docId, attachments) -> attachments.keys.map { docId to it } }

	override suspend fun upload(entity: T, attachmentId: String, content: Flow<DataBuffer>, userId: String): Boolean =
		unsafeUpload(entity.id, attachmentId, content, userId)

	override suspend fun unsafeUpload(entityId: String, attachmentId: String, content: Flow<DataBuffer>, userId: String): Boolean {
		checkUser(userId)
		return if (available) {
			entityToAttachments.computeIfAbsent(entityId) { mutableMapOf() }.putIfAbsent(attachmentId, content.toByteArray(true))
			eventsChannel.send(ObjectStoreEvent(entityId, attachmentId, ObjectStoreEvent.Type.SUCCESSFUL_UPLOAD))
			true
		} else {
			eventsChannel.send(ObjectStoreEvent(entityId, attachmentId, ObjectStoreEvent.Type.UNSUCCESSFUL_UPLOAD))
			false
		}
	}

	override fun get(entity: T, attachmentId: String, userId: String): Flow<DataBuffer> {
		checkUser(userId)
		return if (available) {
			entityToAttachments[entity.id]?.get(attachmentId)?.let { flowOf(DefaultDataBufferFactory.sharedInstance.wrap(it)) }
				?: throw IllegalStateException("Document does not exist. Available attachments: $attachmentsKeys")
		} else throw ObjectStorageException("Storage service is unavailable", null)
	}

	override suspend fun checkAvailable(entity: T, attachmentId: String, userId: String): Boolean {
		checkUser(userId)
		return entityToAttachments[entity.id]?.let { attachmentId in it } == true
	}

	override suspend fun delete(entity: T, attachmentId: String, userId: String): Boolean {
		checkUser(userId)
		return unsafeDelete(entity.id, attachmentId, userId)
	}

	override suspend fun unsafeDelete(entityId: String, attachmentId: String, userId: String): Boolean {
		checkUser(userId)
		return if (available) {
			entityToAttachments[entityId]?.remove(attachmentId)
			eventsChannel.send(ObjectStoreEvent(entityId, attachmentId, ObjectStoreEvent.Type.SUCCESSFUL_DELETE))
			true
		} else {
			eventsChannel.send(ObjectStoreEvent(entityId, attachmentId, ObjectStoreEvent.Type.UNSUCCESSFUL_DELETE))
			false
		}
	}

	data class ObjectStoreEvent(val documentId: String, val attachmentId: String, val type: Type) {
		enum class Type { SUCCESSFUL_UPLOAD, SUCCESSFUL_DELETE, UNSUCCESSFUL_UPLOAD, UNSUCCESSFUL_DELETE }
	}

	private fun checkUser(user: String) {
		user shouldBeIn userIds
	}
}
