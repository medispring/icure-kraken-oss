package org.taktik.icure.test.fakedaos

import java.net.URI
import kotlinx.coroutines.flow.Flow
import org.taktik.icure.asyncdao.CalendarItemTypeDAO
import org.taktik.icure.asyncdao.GenericDAO
import org.taktik.icure.entities.CalendarItemType
import org.taktik.icure.test.FakeGenericDAO

class FakeCalendarItemTypeDAO: CalendarItemTypeDAO, GenericDAO<CalendarItemType> by FakeGenericDAO() {
	// Implement as needed by tests
	override fun getCalendarItemsWithDeleted(): Flow<CalendarItemType> {
		TODO("Not yet implemented")
	}
}

