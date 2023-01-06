package org.taktik.icure.test.fakedaos

import java.net.URI
import kotlinx.coroutines.flow.Flow
import org.taktik.icure.asyncdao.CalendarItemDAO
import org.taktik.icure.asyncdao.GenericDAO
import org.taktik.icure.entities.CalendarItem
import org.taktik.icure.test.FakeGenericDAO

class FakeCalendarItemDAO : CalendarItemDAO, GenericDAO<CalendarItem> by FakeGenericDAO() {
	// Implement as needed by tests
	override fun listCalendarItemByStartDateAndHcPartyId(startDate: Long?, endDate: Long?, hcPartyId: String): Flow<CalendarItem> {
		TODO("Not yet implemented")
	}

	override fun listCalendarItemByStartDateAndAgendaId(startDate: Long?, endDate: Long?, agendaId: String): Flow<CalendarItem> {
		TODO("Not yet implemented")
	}

	override fun listCalendarItemByEndDateAndHcPartyId(startDate: Long?, endDate: Long?, hcPartyId: String): Flow<CalendarItem> {
		TODO("Not yet implemented")
	}

	override fun listCalendarItemByEndDateAndAgendaId(startDate: Long?, endDate: Long?, agenda: String): Flow<CalendarItem> {
		TODO("Not yet implemented")
	}

	override fun listCalendarItemByPeriodAndHcPartyId(startDate: Long?, endDate: Long?, hcPartyId: String): Flow<CalendarItem> {
		TODO("Not yet implemented")
	}

	override fun listCalendarItemByPeriodAndAgendaId(startDate: Long?, endDate: Long?, agendaId: String): Flow<CalendarItem> {
		TODO("Not yet implemented")
	}

	override fun listAccessLogsByHcPartyAndPatient(hcPartyId: String, secretPatientKeys: List<String>): Flow<CalendarItem> {
		TODO("Not yet implemented")
	}

	override fun listCalendarItemsByRecurrenceId(recurrenceId: String): Flow<CalendarItem> {
		TODO("Not yet implemented")
	}
}
