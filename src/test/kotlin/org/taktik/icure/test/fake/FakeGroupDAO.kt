package org.taktik.icure.test.fake

import java.net.URI
import kotlinx.coroutines.flow.Flow
import org.taktik.couchdb.Client
import org.taktik.couchdb.ViewQueryResultEvent
import org.taktik.couchdb.entity.ComplexKey
import org.taktik.couchdb.entity.Option
import org.taktik.icure.asyncdao.GroupDAO
import org.taktik.icure.db.PaginationOffset
import org.taktik.icure.entities.Group

class FakeGroupDAO: GroupDAO {
	// Implement as needed by tests
	override fun getAll(): Flow<Group> {
		TODO("Not yet implemented")
	}

	override fun getDeleted(): Flow<Group> {
		TODO("Not yet implemented")
	}

	override suspend fun save(group: Group): Group? {
		TODO("Not yet implemented")
	}

	override fun getList(ids: Flow<String>): Flow<Group> {
		TODO("Not yet implemented")
	}

	override fun getAllIds(): Flow<String> {
		TODO("Not yet implemented")
	}

	override suspend fun get(id: String, vararg options: Option) = Group(id = id)

	override suspend fun get(id: String, rev: String?, vararg options: Option) = Group(id = id)

	override suspend fun forceInitStandardDesignDocument(dbInstanceUrl: URI, groupId: String?, updateIfExists: Boolean) {
		TODO("Not yet implemented")
	}

	override suspend fun forceInitStandardDesignDocument(client: Client, updateIfExists: Boolean) {
		TODO("Not yet implemented")
	}

	override fun findChildrenGroups(groupId: String, startDocumentId: String?, limit: Int): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}

	override fun findChildrenGroupsByContent(groupId: String, searchString: String, paginationOffset: PaginationOffset<ComplexKey>): Flow<ViewQueryResultEvent> {
		TODO("Not yet implemented")
	}
}
