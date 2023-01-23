package org.taktik.icure.test.fakedaos

import java.net.URI
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import org.taktik.icure.asyncdao.GenericDAO
import org.taktik.icure.asyncdao.TimeTableDAO
import org.taktik.icure.entities.TimeTable
import org.taktik.icure.test.FakeGenericDAO

class FakeTimeTableDAO : TimeTableDAO, GenericDAO<TimeTable> by FakeGenericDAO() {
	// Implement as needed by tests
	override fun listTimeTableByPeriodAndAgendaId(startDate: Long?, endDate: Long?, agendaId: String) =
		getEntities().filter { it.agendaId == agendaId }

	override fun listTimeTableByAgendaId(agendaId: String): Flow<TimeTable> {
		TODO("Not yet implemented")
	}

	override fun listTimeTableByAgendaIds(agendaIds: Collection<String>)
		= getEntities().filter { agendaIds.contains(it.agendaId) }

	override fun listTimeTableByStartDateAndAgendaId(startDate: Long?, endDate: Long?, agendaId: String): Flow<TimeTable> {
		TODO("Not yet implemented")
	}

	override fun listTimeTableByEndDateAndAgendaId(startDate: Long?, endDate: Long?, agendaId: String): Flow<TimeTable> {
		TODO("Not yet implemented")
	}

	override fun listTimeTableByPeriodAndAgendaIds(
        startDate: Long?,
        endDate: Long?,
        agendaIds: Set<String>
    ) = getEntities().filter { agendaIds.contains(it.agendaId) }
}
