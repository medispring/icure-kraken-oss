/*
 * Copyright (c) 2020. Taktik SA, All rights reserved.
 */
package org.taktik.icure.asynclogic.impl.filter.maintenancetask

import javax.security.auth.login.LoginException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.springframework.stereotype.Service
import org.taktik.icure.asynclogic.AsyncSessionLogic
import org.taktik.icure.asynclogic.MaintenanceTaskLogic
import org.taktik.icure.asynclogic.impl.filter.Filter
import org.taktik.icure.asynclogic.impl.filter.Filters
import org.taktik.icure.domain.filter.maintenancetask.MaintenanceTaskAfterDateFilter
import org.taktik.icure.entities.MaintenanceTask
import org.taktik.icure.utils.getLoggedDataOwnerId

@Service
class MaintenanceTaskAfterDateFilter(
	private val maintenanceTaskLogic: MaintenanceTaskLogic,
	private val sessionLogic: AsyncSessionLogic
	) : Filter<String, MaintenanceTask, MaintenanceTaskAfterDateFilter> {

	@OptIn(ExperimentalCoroutinesApi::class)
	override fun resolve(
		filter: MaintenanceTaskAfterDateFilter,
		context: Filters
	) = flow {
		try {
			emitAll(maintenanceTaskLogic.listMaintenanceTasksAfterDate(filter.healthcarePartyId ?: getLoggedDataOwnerId(sessionLogic), filter.date))
		} catch (e: LoginException) {
			throw IllegalArgumentException(e)
		}
	}
}
