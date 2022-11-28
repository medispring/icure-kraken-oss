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

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import org.springframework.stereotype.Service
import org.taktik.couchdb.DocIdentifier
import org.taktik.icure.asyncdao.TimeTableDAO
import org.taktik.icure.asynclogic.AsyncSessionLogic
import org.taktik.icure.asynclogic.TimeTableLogic
import org.taktik.icure.entities.TimeTable

@Service
class TimeTableLogicImpl(private val timeTableDAO: TimeTableDAO, private val sessionLogic: AsyncSessionLogic) : GenericLogicImpl<TimeTable, TimeTableDAO>(sessionLogic), TimeTableLogic {
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
		val (dbInstanceUri, groupId) = sessionLogic.getInstanceAndGroupInformationFromSecurityContext()
		emitAll(timeTableDAO.listTimeTablesByPeriodAndAgendaId(dbInstanceUri, groupId, startDate, endDate, agendaId))
	}

	override fun getCalendarItemTypeIdsAndLocationsByGroupIdAndAgendaIds(groupId: String, startDate: Long, endDate: Long, agendaIds: Collection<String>) = flow {
		val group = groupDAO.get(groupId) ?: throw IllegalArgumentException("Invalid groupId")
		val uri = group.servers?.firstOrNull()?.let { URI(it) } ?: dbInstanceUri
		timeTableDAO.listTimeTablesByPeriodAndAgendaIds(uri, groupId, startDate, endDate, agendaIds).collect { tt ->
			tt.items.filter { tti -> tti.publicTimeTableItem }.mapNotNull { tti -> tti.calendarItemTypeId?.let { Triple(it, tti.placeId, tti.acceptsNewPatient) } }.toSet().forEach { emit(it) }
		}
	}

	//This method is supposed to be insecure because it is the only way an anonymous user can list the availabilities of a hcp
	override fun getAvailabilitiesByPeriodAndCalendarItemTypeId(groupId: String, userId: String, startDate: Long, endDate: Long, calendarItemTypeId: String, placeId: String?, isNewPatient: Boolean, publicTimeTablesOnly: Boolean, hcpId: String, limit: Int): Flow<Long> = flow {
		val group = groupDAO.get(groupId) ?: throw IllegalArgumentException("Invalid groupId")
		val uri = group.servers?.firstOrNull()?.let { URI(it) } ?: dbInstanceUri

		val startLdt = FuzzyValues.getDateTime(startDate - (startDate % 100))
		val endLdt = FuzzyValues.getDateTime(endDate - (endDate % 100))

		val agendaIds = agendaLogic.getAnonymousAgendasByUser(groupId, userId).map { it.id }.distinct()

		val cit = calendarItemTypeLogic.getAnonymousCalendarItemTypes(groupId, listOf(calendarItemTypeId)).firstOrNull() ?: throw IllegalArgumentException("Invalid calendar item type id")
		val cis = agendaIds.map { calendarItemLogic.getCalendarItemByPeriodAndAgendaId(groupId, startDate, endDate, it) }.flattenConcat().toList()
		val tts = timeTableDAO.listTimeTablesByPeriodAndAgendaIds(uri, groupId, startDate, endDate, agendaIds.toSet()).filter {
			!publicTimeTablesOnly || it.items.any { tti -> tti.publicTimeTableItem }
		}.toList()

		val secs = cit.duration.toLong() * 60 //s
		val duration = Duration.ofSeconds(secs)
		val coercedEndLdt = (startLdt + Duration.ofDays(120)).coerceAtMost(endLdt)

		tailrec fun feedSlots(slotDtStart: LocalDateTime, filtered: List<Long>): List<Long> =
			if (filtered.size == limit || slotDtStart > coercedEndLdt) {
				filtered
			} else {
				val slotDtEnd = slotDtStart + duration
				val dow = slotDtStart.dayOfWeek
				val slotSecStart = slotDtStart.hour * 3600 + slotDtStart.minute * 60 + slotDtStart.second
				val slotSecEnd = (slotDtEnd.hour * 3600 + slotDtEnd.minute * 60 + slotDtEnd.second).let { t ->
					t.takeIf { it > slotSecStart } ?: (t + 24 * 3600)
				}

				val slotStartLong = FuzzyValues.getFuzzyDateTime(slotDtStart, ChronoUnit.SECONDS)
				val slotEndLong = FuzzyValues.getFuzzyDateTime(slotDtEnd, ChronoUnit.SECONDS)

				val ttis = tts.filter { (it.startTime ?: 0) <= slotStartLong && (it.endTime ?: Long.MAX_VALUE) >= slotEndLong }
					.flatMap { tt -> tt.items.filter { tti -> tti.publicTimeTableItem && tti.calendarItemTypeId == calendarItemTypeId && (placeId == null || tti.placeId == placeId) && (tti.acceptsNewPatient || !isNewPatient) } }
				val rrulesIterators = ttis.mapNotNull { it.rrule }.associateWith {
					RecurrenceRule(it).iterator(slotDtStart.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli() - 24 * 3600 * 1000, TimeZone.getTimeZone("UTC"))
				}

				//ttis now hold all eligible slots


				val coercedSlotDtStart = tts.mapNotNull { tt ->
					if ((tt.startTime ?: 0) <= slotStartLong && (tt.endTime ?: Long.MAX_VALUE) >= slotEndLong) {
						tt.items.mapNotNull { tti ->
							if (
								(tti.rrule?.let {
									val rri = rrulesIterators[it]!!
									while (FuzzyValues.getFuzzyDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(rri.peekMillis() + 24*3600*1000), ZoneId.of("UTC")), ChronoUnit.SECONDS) <= slotStartLong) {
										rri.nextMillis()
									}
									val nextSlot = FuzzyValues.getFuzzyDateTime(LocalDateTime.ofInstant(Instant.ofEpochMilli(rri.peekMillis()), ZoneId.of("UTC")), ChronoUnit.SECONDS)
									nextSlot < slotStartLong
								} == true || (tti.days.any { dd ->
									dd.toInt() == dow.value
								} && //The day of week of the timestamp is listed in the days property
    								tti.recurrenceTypes.any { r -> //The day of the week of the slot matches a weekly recurrence condition
									r == "EVERY_WEEK" || listOf("ONE" to 1, "TWO" to 2, "THREE" to 3, "FOUR" to 4, "FIVE" to 5).any { (rt, i) ->
										(r == rt && isXDayweekOfMonthInRange(dow, i.toLong(), startLdt, coercedEndLdt))
									}
								}))
							) {
								tti.hours.mapNotNull { h -> //The time of the slot matches a hours span
									val ttSecStart = (((h.startHour?.toHms())?.toSec() ?: 0) / 60) * 60
									val ttSecEnd = (((h.endHour?.toHms()?.toSec() ?: (24 * 3600)) / 60) * 60).let { t ->
										t.takeIf { it > ttSecStart } ?: (t + 24 * 3600)
									}

									if (ttSecStart <= slotSecStart) {
										val delta = slotSecStart - ttSecStart
										val correction = if (delta % secs == 0L) 0 else secs - (delta % secs)
										if (ttSecEnd >= slotSecEnd + correction) {
											slotDtStart + Duration.ofSeconds(correction)
										} else null
									} else null
								}.firstOrNull()
							} else null
						}.firstOrNull()
					} else null //The slot is inside the tt boundaries
				}.firstOrNull()

				if (coercedSlotDtStart == null) {
					feedSlots(slotDtStart + Duration.ofMinutes(1), filtered)
				} else {
					val coercedSlotStartLong = FuzzyValues.getFuzzyDateTime(coercedSlotDtStart, ChronoUnit.SECONDS)
					val coercedSlotEndLong = FuzzyValues.getFuzzyDateTime(coercedSlotDtStart + Duration.ofSeconds(secs), ChronoUnit.SECONDS)

					if (
						!cis
							.filter { it.startTime != null }
							.any { ci -> //No existing ci conflicts
								(ci.startTime ?: 0) < coercedSlotEndLong && (
									ci.endTime ?: (
										(ci.duration ?: 60).let { d ->
											FuzzyValues.getFuzzyDateTime(FuzzyValues.getDateTime(ci.startTime ?: 0) + Duration.ofSeconds(d * 60), ChronoUnit.SECONDS)
										}
										)
									) > coercedSlotStartLong
							} //We could use some idx here to remember which was the latest ci that matched and start at this one as slots and cis are sorted chronologically
					) {
						feedSlots(coercedSlotDtStart + Duration.ofSeconds(secs), filtered + FuzzyValues.getFuzzyDateTime(coercedSlotDtStart, ChronoUnit.SECONDS))
					} else feedSlots(coercedSlotDtStart + Duration.ofSeconds(secs), filtered)
				}
			}

		feedSlots(startLdt, persistentListOf<Long>() as List<Long>).forEach { emit(it) }
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
