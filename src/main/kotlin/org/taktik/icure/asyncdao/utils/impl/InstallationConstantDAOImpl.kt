package org.taktik.icure.asyncdao.utils.impl

import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.springframework.stereotype.Service
import org.taktik.couchdb.annotation.View
import org.taktik.couchdb.id.IDGenerator
import org.taktik.icure.asyncdao.impl.CouchDbDispatcher
import org.taktik.icure.asyncdao.impl.InternalDAOImpl
import org.taktik.icure.asyncdao.utils.InstallationConstantDAO
import org.taktik.icure.entities.utils.InstallationConstant
import org.taktik.icure.properties.CouchDbProperties

@OptIn(ExperimentalCoroutinesApi::class)
@Service
@View(name = "all", map = "function(doc) { if (doc.java_type == 'org.taktik.icure.entities.utils.InstallationConstant' && !doc.deleted) emit( null, doc._id )}")
class InstallationConstantDAOImpl(
	couchDbProperties: CouchDbProperties,
	systemCouchDbDispatcher: CouchDbDispatcher,
	idGenerator: IDGenerator
) : InternalDAOImpl<InstallationConstant>(
	InstallationConstant::class.java,
	couchDbProperties,
	systemCouchDbDispatcher,
	idGenerator
), InstallationConstantDAO
