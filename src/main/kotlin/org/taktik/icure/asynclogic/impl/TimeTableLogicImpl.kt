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
package org.taktik.icure.asynclogic.impl

import java.time.Duration
import java.time.temporal.ChronoUnit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.toSet
import org.springframework.stereotype.Service
import org.taktik.couchdb.DocIdentifier
import org.taktik.icure.asyncdao.TimeTableDAO
import org.taktik.icure.asynclogic.AgendaLogic
import org.taktik.icure.asynclogic.AsyncSessionLogic
import org.taktik.icure.asynclogic.CalendarItemLogic
import org.taktik.icure.asynclogic.CalendarItemTypeLogic
import org.taktik.icure.asynclogic.TimeTableLogic
import org.taktik.icure.entities.TimeTable
import org.taktik.icure.utils.FuzzyValues
import org.taktik.icure.utils.distinct
import org.taktik.icure.utils.entities.embed.iterator
import org.taktik.icure.utils.map
import org.taktik.icure.utils.sortedMerge

class SlotAndAgenda(val slot: Long, val agendaId: String?): Comparable<SlotAndAgenda> {
	override fun compareTo(other: SlotAndAgenda) = compareBy<SlotAndAgenda>({ it.slot }, { it.agendaId }).compare(this, other)
	operator fun component1() = slot
	operator fun component2() = agendaId
}

@Service
class TimeTableLogicImpl(
	private val timeTableDAO: TimeTableDAO,
	private val agendaLogic: AgendaLogic,
	private val calendarItemTypeLogic: CalendarItemTypeLogic,
	private val calendarItemLogic: CalendarItemLogic,
	sessionLogic: AsyncSessionLogic
) : GenericLogicImpl<TimeTable, TimeTableDAO>(sessionLogic), TimeTableLogic {
	override suspend fun createTimeTable(timeTable: TimeTable) = fix(timeTable) { timeTable ->
		timeTableDAO.create(timeTable)
	}

	override fun deleteTimeTables(ids: List<String>): Flow<DocIdentifier> {
		return deleteEntities(ids)
	}

	override suspend fun getTimeTable(timeTableId: String): TimeTable? {
		return timeTableDAO.get(timeTableId)
	}

	override fun getTimeTablesByPeriodAndAgendaId(startDate: Long, endDate: Long, agendaId: String): Flow<TimeTable> = flow {
		emitAll(timeTableDAO.listTimeTableByPeriodAndAgendaId(startDate, endDate, agendaId))
	}

	//This method is supposed to be insecure because it is the only way an anonymous user can list the availabilities of a hcp
	override fun getAvailabilitiesByPeriodAndCalendarItemTypeId(userId: String, startDate: Long, endDate: Long, calendarItemTypeId: String, placeId: String?, isNewPatient: Boolean, publicTimeTablesOnly: Boolean, hcpId: String, limit: Int): Flow<Long> = flow {
		val roundedStartDate = startDate - (startDate % 100)
		val roundedEndDate = endDate - (endDate % 100)

		val startLdt = FuzzyValues.getDateTime(roundedStartDate)
		val endLdt = FuzzyValues.getDateTime(roundedEndDate)

		val agendaIds = agendaLogic.getAgendasByUser(userId).map { it.id }.distinct()

		val cit = calendarItemTypeLogic.getCalendarItemTypes(listOf(calendarItemTypeId)).firstOrNull() ?: throw IllegalArgumentException("Invalid calendar item type id")
		val cis = agendaIds.map { calendarItemLogic.getCalendarItemByPeriodAndAgendaId(startDate, endDate, it) }.flattenConcat().toList()
		val tts = timeTableDAO.listTimeTableByPeriodAndAgendaIds(startDate, endDate, agendaIds.toSet()).filter {
			!publicTimeTablesOnly || it.items.any { tti -> tti.publicTimeTableItem }
		}.toList()

		val secs = (cit.duration.toLong() * 60).coerceAtLeast(60) //s
		val duration = Duration.ofSeconds(secs)
		val coercedEndLdt = (startLdt + Duration.ofDays(120)).coerceAtMost(endLdt)
		val coercedEnd = FuzzyValues.getFuzzyDateTime(coercedEndLdt, ChronoUnit.SECONDS)

		val ttis = tts.filter { (it.startTime ?: 0) <= roundedEndDate && (it.endTime ?: Long.MAX_VALUE) >= roundedStartDate }
			.flatMap { tt -> tt.items
				.filter { tti -> tti.publicTimeTableItem && tti.calendarItemTypeId == calendarItemTypeId && (placeId == null || tti.placeId == placeId) && (tti.acceptsNewPatient || !isNewPatient) }
				.map { it to tt }
			}
		val iterator = ttis.map { (tti, tt) -> tti.iterator(roundedStartDate, roundedEndDate, duration).map { SlotAndAgenda(it, tt.agendaId) } }.sortedMerge()

		fun nextSlot(): Long? = (if (iterator.hasNext()) iterator.next() else null)?.let { (start, agendaId) ->
			val end = FuzzyValues.getFuzzyDateTime(FuzzyValues.getDateTime(start) + Duration.ofSeconds(secs), ChronoUnit.SECONDS)

			when {
				start > coercedEnd -> {
					null
				}

				!cis.filter { it.startTime != null && it.agendaId == agendaId }
					.any { ci -> //No existing ci conflicts
						(ci.startTime ?: 0) < end && (
							ci.endTime ?: (
								(ci.duration ?: 60).let { d ->
									FuzzyValues.getFuzzyDateTime(
										FuzzyValues.getDateTime(ci.startTime ?: 0) + Duration.ofSeconds(d * 60), ChronoUnit.SECONDS
									)
								})
							) > start
					} -> {
					start
				}

				else -> {
					nextSlot()
				}
			}
		}

		generateSequence {
			nextSlot()
		}.take(limit).forEach {
			emit(it)
		}
	}

	override fun getTimeTablesByAgendaId(agendaId: String): Flow<TimeTable> = flow {
		emitAll(timeTableDAO.listTimeTableByAgendaId(agendaId))
	}

	override suspend fun modifyTimeTable(timeTable: TimeTable) = fix(timeTable) { timeTable ->
		timeTableDAO.save(timeTable)
	}

	override fun getGenericDAO(): TimeTableDAO {
		return timeTableDAO
	}
}
