package org.taktik.icure.test

import java.lang.IllegalStateException
import java.net.URI
import java.nio.ByteBuffer
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import org.apache.http.HttpStatus
import org.taktik.couchdb.BulkUpdateResult
import org.taktik.couchdb.Client
import org.taktik.couchdb.DocIdentifier
import org.taktik.couchdb.entity.Option
import org.taktik.couchdb.entity.Versionable
import org.taktik.couchdb.exception.CouchDbConflictException
import org.taktik.couchdb.exception.CouchDbException
import org.taktik.icure.asyncdao.GenericDAO
import org.taktik.icure.asyncdao.InternalDAO

class FakeGenericDAO<T : Versionable<String>> : GenericDAO<T> {
	private val allLatestEntitiesMap =
		ConcurrentHashMap<String, T>()
	private val allVersionedEntitiesMap =
		ConcurrentHashMap<String, ConcurrentHashMap<String, T>>()

	private fun latestEntitiesMap(): ConcurrentHashMap<String, T> =
		allLatestEntitiesMap
	private fun versionedEntitiesMap(): ConcurrentHashMap<String, ConcurrentHashMap<String, T>> =
		allVersionedEntitiesMap

	override fun getEntities(): Flow<T> =
		latestEntitiesMap().values.asFlow()

	override fun getEntityIds(limit: Int?): Flow<String> =
		latestEntitiesMap().keys.toList().let { if (limit != null) it.take(limit) else it }.asFlow()

	override suspend fun forceInitStandardDesignDocument(updateIfExists: Boolean, useVersioning: Boolean) {
	}

	override suspend fun forceInitStandardDesignDocument(client: Client, updateIfExists: Boolean, useVersioning: Boolean) {
	}

	override suspend fun initSystemDocumentIfAbsent(dbInstanceUrl: URI) {
	}

	override suspend fun initSystemDocumentIfAbsent(client: Client) {
	}

	override suspend fun get(id: String, vararg options: Option): T? =
		get(id, null, *options)

	override suspend fun get(id: String, rev: String?, vararg options: Option): T? {
		require(options.isEmpty()) { "Options are not supported by mock" }
		return if (rev == null) latestEntitiesMap()[id] else versionedEntitiesMap()[id]?.get(rev)
	}

	override fun getEntities(ids: Collection<String>): Flow<T> {
		val idsSet = ids.toSet()
		return latestEntitiesMap().toList().asFlow().filter { it.first in idsSet }.map { it.second }
	}

	override fun getEntities(ids: Flow<String>): Flow<T> = flow {
		emitAll(getEntities(ids.toList()))
	}

	override fun <K : Collection<T>> create(entities: K): Flow<T> =
		save(entities)

	override suspend fun create(entity: T): T? =
		save(entity)

	override suspend fun save(entity: T): T {
		@Suppress("UNCHECKED_CAST")
		val entityWithNewRev = entity.withIdRev(null, entity.rev?.let { (it.toInt() + 1).toString() } ?: "0") as T
		val newValue = latestEntitiesMap().compute(entityWithNewRev.id) { _, prev ->
			if (prev?.rev == entity.rev) entityWithNewRev else prev
		}
		return if (newValue == entityWithNewRev) {
			versionedEntitiesMap().compute(entityWithNewRev.id) { _, prev -> prev ?: ConcurrentHashMap() }!!
				.put(entityWithNewRev.rev!!, entityWithNewRev)
			newValue
		} else throw CouchDbConflictException("Current rev is ${newValue?.rev}", HttpStatus.SC_CONFLICT, "Rev conflict")
	}

	override fun <K : Collection<T>> save(entities: K): Flow<T> = flow {
		entities.map { save(it) }.forEach { emit(it) }
	}

	override suspend fun purge(entity: T): DocIdentifier =
		latestEntitiesMap().remove(entity.id).let { DocIdentifier(it?.id, it?.rev) }.also {
			versionedEntitiesMap().remove(entity.id)
		}

	override suspend fun purge(entities: Collection<T>) {
		entities.forEach { purge(it) }
	}

	override suspend fun remove(entity: T): DocIdentifier =
		purge(entity)

	override fun remove(entities: Collection<T>): Flow<DocIdentifier> = flow {
		entities.map { remove(it) }.forEach { emit(it) }
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

	override suspend fun contains(id: String): Boolean =
		latestEntitiesMap().containsKey(id)

	override suspend fun hasAny(): Boolean =
		allLatestEntitiesMap.isNotEmpty()

	override fun unRemove(entities: Collection<T>): Flow<DocIdentifier> {
		throw IllegalStateException("Operation not supported")
	}

	override suspend fun unRemove(entity: T): DocIdentifier {
		throw IllegalStateException("Operation not supported")
	}
}
