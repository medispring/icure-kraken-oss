package org.taktik.icure.test

import java.net.URI
import java.nio.ByteBuffer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import org.taktik.couchdb.Client
import org.taktik.couchdb.DocIdentifier
import org.taktik.couchdb.entity.Option
import org.taktik.icure.asyncdao.GenericDAO

class FakeDAOImpl: GenericDAO<FakeDto> {

	var cachedEntities = listOf<FakeDto>()

	fun resetCache() {
		cachedEntities = listOf()
	}

	override fun <K : Collection<FakeDto>> create(entities: K): Flow<FakeDto> {
		cachedEntities = entities.toList()
		return entities.asFlow()
	}

	override fun getAttachment(documentId: String, attachmentId: String, rev: String?): Flow<ByteBuffer> {
		TODO("Not yet implemented")
	}

	override suspend fun createAttachment(documentId: String, attachmentId: String, rev: String, contentType: String, data: Flow<ByteBuffer>): String {
		TODO("Not yet implemented")
	}

	override suspend fun deleteAttachment(documentId: String, rev: String, attachmentId: String): String {
		TODO("Not yet implemented")
	}

	override suspend fun save(entity: FakeDto): FakeDto? {
		TODO("Not yet implemented")
	}

	override suspend fun create(entity: FakeDto): FakeDto? {
		TODO("Not yet implemented")
	}

	override suspend fun contains(id: String): Boolean {
		TODO("Not yet implemented")
	}

	override suspend fun hasAny(): Boolean {
		TODO("Not yet implemented")
	}

	override fun getEntities(): Flow<FakeDto> {
		TODO("Not yet implemented")
	}

	override fun getEntities(ids: Collection<String>): Flow<FakeDto> {
		TODO("Not yet implemented")
	}

	override fun getEntities(ids: Flow<String>): Flow<FakeDto> {
		TODO("Not yet implemented")
	}

	override fun getEntityIds(limit: Int?): Flow<String> {
		TODO("Not yet implemented")
	}

	override suspend fun forceInitStandardDesignDocument(updateIfExists: Boolean, useVersioning: Boolean) {
		TODO("Not yet implemented")
	}

	override suspend fun forceInitStandardDesignDocument(client: Client, updateIfExists: Boolean, useVersioning: Boolean) {
		TODO("Not yet implemented")
	}

	override suspend fun initSystemDocumentIfAbsent(dbInstanceUrl: URI) {
		TODO("Not yet implemented")
	}

	override suspend fun initSystemDocumentIfAbsent(client: Client) {
		TODO("Not yet implemented")
	}

	override suspend fun get(id: String, vararg options: Option): FakeDto? {
		TODO("Not yet implemented")
	}

	override suspend fun get(id: String, rev: String?, vararg options: Option): FakeDto? {
		TODO("Not yet implemented")
	}

	override suspend fun unRemove(entity: FakeDto): DocIdentifier {
		TODO("Not yet implemented")
	}

	override fun unRemove(entities: Collection<FakeDto>): Flow<DocIdentifier> {
		TODO("Not yet implemented")
	}

	override suspend fun purge(entities: Collection<FakeDto>) {
		TODO("Not yet implemented")
	}

	override suspend fun purge(entity: FakeDto): DocIdentifier {
		TODO("Not yet implemented")
	}

	override fun remove(entities: Collection<FakeDto>): Flow<DocIdentifier> {
		TODO("Not yet implemented")
	}

	override suspend fun remove(entity: FakeDto): DocIdentifier {
		TODO("Not yet implemented")
	}

	override fun <K : Collection<FakeDto>> save(entities: K): Flow<FakeDto> {
		TODO("Not yet implemented")
	}
}
