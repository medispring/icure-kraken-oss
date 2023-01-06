package org.taktik.icure.utils.entities.embed

import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.temporal.ChronoUnit
import java.util.TimeZone
import org.dmfs.rfc5545.recur.RecurrenceRule
import org.taktik.icure.entities.embed.TimeTableHour
import org.taktik.icure.entities.embed.TimeTableItem
import org.taktik.icure.utils.FuzzyValues
import org.taktik.icure.utils.isXDayweekOfMonthInRange

fun TimeTableItem.iterator(startDateAndTime: Long, endDateAndTime: Long, duration: Duration) = object : Iterator<Long> {
	val startLdt = FuzzyValues.getDateTime(startDateAndTime - (startDateAndTime % 100))
	val endLdt = FuzzyValues.getDateTime(endDateAndTime - (endDateAndTime % 100))
	val coercedEndLdt = (startLdt + Duration.ofDays(120)).coerceAtMost(endLdt)

	val daysIterator = object : Iterator<LocalDateTime> {
		var day = startLdt
		val rrit = rrule?.let {
			RecurrenceRule(it).iterator(FuzzyValues.getDateTime(rruleStartDate ?: startDateAndTime).atOffset(ZoneOffset.UTC).toInstant().toEpochMilli(), TimeZone.getTimeZone("UTC")).also {
				it.fastForward(FuzzyValues.getDateTime(startDateAndTime).atOffset(ZoneOffset.UTC).toInstant().toEpochMilli() - 24*3600*1000)
			}
		}

		override fun hasNext(): Boolean {
			return rrit?.let {
				try {
					it.peekMillis().let { n ->
						LocalDateTime.ofInstant(java.time.Instant.ofEpochMilli(n), ZoneOffset.UTC) < coercedEndLdt
					}
				} catch(e:ArrayIndexOutOfBoundsException) { false } } ?: (day < coercedEndLdt)
		}
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
				}.also { day = it + Duration.ofDays(1) }
			}
		}
	}

	var hoursIterator = hours.iterator(duration)

	var currentDay = if (daysIterator.hasNext()) daysIterator.next() else null

	init {
		val startOfStartDayLdt = startLdt.withHour(0).withMinute(0).withSecond(0)
		while (currentDay != null && currentDay!! < startOfStartDayLdt) {
			currentDay = if (daysIterator.hasNext()) daysIterator.next() else null
		}
	}

	override fun hasNext(): Boolean {
		return if (currentDay != null && hoursIterator.hasNext()) true else {
			hoursIterator = hours.iterator(duration)
			currentDay = if (daysIterator.hasNext()) daysIterator.next() else null
			(currentDay != null && hoursIterator.hasNext())
		}
	}
	override fun next(): Long = if (hoursIterator.hasNext()) {
		hoursIterator.next().let {
			FuzzyValues.getFuzzyDateTime(currentDay!! + Duration.ofNanos(it.toLocalTime().toNanoOfDay()), ChronoUnit.SECONDS)
		}
	} else {
		hoursIterator = hours.iterator(duration)
		currentDay = if (daysIterator.hasNext()) daysIterator.next() else null
		next()
	}
}

fun List<TimeTableHour>.iterator(duration: Duration) = object : Iterator<Long> {
	var current: LocalTime? = this@iterator.firstOrNull()?.let { (it.startHour ?: 0L).toLocalTime() } ?: throw NoSuchElementException()
	var idx = 0

	init {
		if (!hasNext()) throw NoSuchElementException()
	}

	override fun hasNext() = current != null
	override fun next() = (current ?: throw NoSuchElementException()).let { c ->
		c.toLongHour().also {
			current = (c + duration).let {
				when {
					it.toLongHour() < (this@iterator[idx].endHour ?: 240000) -> it
					idx < this@iterator.size - 1 -> {
						idx++
						(this@iterator[idx].startHour ?: 0L).toLocalTime()
					}
					else -> null
				}
			}
		}
	}
}

fun LocalTime.toLongHour() = (this.hour * 10000 + this.minute * 100 + this.second).toLong()
fun Long.toLocalTime(): LocalTime = LocalTime.of((this / 10000).toInt(), ((this % 10000) / 100).toInt(), (this % 100).toInt())
