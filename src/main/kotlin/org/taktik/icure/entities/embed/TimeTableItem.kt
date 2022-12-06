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
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.pozo.KotlinBuilder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder

/**
 * @property rrule a RFC-5545 recurrence rule specifying the days and recurrence type of the timetable item. ("RRULE:FREQ=WEEKLY;UNTIL=20220930T150400Z;COUNT=30;INTERVAL=2;WKST=MO;BYDAY=TH" = every 2 weeks on Thursday until 30 September 2022.)
 * Note: The RFC-5545 rrule is used only to manage the days of the occurrences. The hours and durations of the appointments are specified in the property .hours.
 */

data class TimeTableItem(
	val occurrenceStartDate: Long? = null, // YYYYMMDD
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
) : Serializable
