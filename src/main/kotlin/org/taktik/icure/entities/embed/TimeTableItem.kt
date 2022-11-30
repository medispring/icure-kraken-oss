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
package org.taktik.icure.entities.embed

import java.io.Serializable
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.TimeZone
import kotlin.time.Duration.Companion.days
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.pozo.KotlinBuilder
import org.dmfs.rfc5545.recur.RecurrenceRule
import org.taktik.icure.utils.FuzzyValues
import org.taktik.icure.utils.isXDayweekOfMonthInRange

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder

/**
 * @property rrule a RFC-5545 recurrence rule specifying the days and recurrence type of the timetable item. ("RRULE:FREQ=WEEKLY;UNTIL=20220930T150400Z;COUNT=30;INTERVAL=2;WKST=MO;BYDAY=TH" = every 2 weeks on Thursday until 30 September 2022.)
 * Note: The RFC-5545 rrule is used only to manage the days of the occurrences. The hours and durations of the appointments are specified in the property .hours.
 */

data class TimeTableItem(
	val occurenceStartDate: Long? = null, // YYYYMMDD
	val rrule: String? = null,
	@Deprecated("Will be replaced by rrule") val days: List<String> = emptyList(),
	@Deprecated("Will be replaced by rrule") val recurrenceTypes: List<String> = emptyList(),
	val hours: List<TimeTableHour> = emptyList(),
	val calendarItemTypeId: String? = null,
	@JsonProperty("isHomeVisit") val homeVisit: Boolean = false,
	val placeId: String? = null,
	val publicTimeTableItem: Boolean = false,
	val acceptsNewPatient: Boolean = true,
	@JsonProperty("isUnavailable") val unavailable: Boolean = false
) : Serializable {
	fun iterator(startDateAndTime: Long, endDateAndTime: Long, duration: Duration) = object:Iterator<Long> {
		val startLdt = FuzzyValues.getDateTime(startDateAndTime - (startDateAndTime % 100))
		val endLdt = FuzzyValues.getDateTime(endDateAndTime - (endDateAndTime % 100))
		val coercedEndLdt = (startLdt + Duration.ofDays(120)).coerceAtMost(endLdt)

		val daysIterator = object:Iterator<LocalDateTime> {
			var day = startLdt
			val rrit = rrule?.let { RecurrenceRule(it).iterator(FuzzyValues.getDateTime(occurenceStartDate ?: startDateAndTime).atOffset(ZoneOffset.UTC).toInstant().toEpochMilli() - 24 * 3600 * 1000, TimeZone.getTimeZone("UTC")) }
			override fun hasNext() = day < coercedEndLdt
			override fun next(): LocalDateTime {
				return rrit?.nextMillis()?.let {
					LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(it), ZoneOffset.UTC).also { day = it }
				} ?: run {
					generateSequence(day) { (it + Duration.ofDays(1)).takeIf { it <= coercedEndLdt } }.first { d ->
						(days.any { dd ->
							dd.toInt() == d.dayOfWeek.value
						} && //The day of week of the timestamp is listed in the days property
							recurrenceTypes.any { r -> //The day of the week of the slot matches a weekly recurrence condition
								r == "EVERY_WEEK" || listOf("ONE" to 1, "TWO" to 2, "THREE" to 3, "FOUR" to 4, "FIVE" to 5).any { (rt, i) ->
									(r == rt && isXDayweekOfMonthInRange(d.dayOfWeek, i.toLong(), startLdt, coercedEndLdt))
								}
							})
					}.also { day = it }
				}
			}
		}

		var hoursIterator = hours.iterator()

		var currentDay = daysIterator.next()

		override fun hasNext() = true
		override fun next() = hoursIterator.next()?.let {
			val next = currentDay.atOffset(ZoneOffset.UTC).toInstant().toEpochMilli() + Duration.ofHours((it.startHour ?: 0) % 10000)
			next
		} ?: run {
			hoursIterator = hours.iterator()
			currentDay = daysIterator.next()
			next()
		}
	}
}
