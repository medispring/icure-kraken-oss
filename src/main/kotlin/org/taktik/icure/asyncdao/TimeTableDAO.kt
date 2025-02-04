/*
 *  iCure Data Stack. Copyright (c) 2020 Taktik SA
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful, but
 *     WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public
 *     License along with this program.  If not, see
 *     <https://www.gnu.org/licenses/>.
 */

package org.taktik.icure.asyncdao

import kotlinx.coroutines.flow.Flow
import org.taktik.icure.entities.TimeTable

interface TimeTableDAO : GenericDAO<TimeTable> {
	fun listTimeTableByAgendaId(agendaId: String): Flow<TimeTable>
	fun listTimeTableByAgendaIds(agendaIds: Collection<String>): Flow<TimeTable>
	fun listTimeTableByStartDateAndAgendaId(startDate: Long?, endDate: Long?, agendaId: String): Flow<TimeTable>
	fun listTimeTableByEndDateAndAgendaId(startDate: Long?, endDate: Long?, agendaId: String): Flow<TimeTable>
	fun listTimeTableByPeriodAndAgendaId(startDate: Long?, endDate: Long?, agendaId: String): Flow<TimeTable>
	fun listTimeTableByPeriodAndAgendaIds(startDate: Long?, endDate: Long?, agendaIds: Set<String>): Flow<TimeTable>
}
