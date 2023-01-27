package org.taktik.icure.asynclogic

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import org.taktik.icure.asynclogic.impl.TimeTableLogicImpl
import org.taktik.icure.entities.Agenda
import org.taktik.icure.entities.CalendarItem
import org.taktik.icure.entities.CalendarItemType
import org.taktik.icure.entities.TimeTable
import org.taktik.icure.entities.base.CodeStub
import org.taktik.icure.entities.embed.TimeTableHour
import org.taktik.icure.entities.embed.TimeTableItem
import org.taktik.icure.test.fake.FakeTimeTableDAO
import org.taktik.icure.test.fake.SessionMock
import org.taktik.icure.test.newId
import org.taktik.icure.test.randomUri

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class TimeTableLogicTest : StringSpec({
	val hcpId = newId()
	val uri = randomUri()
	val groupId = newId()
	val agendaId = newId()
	lateinit var sessionMock: SessionMock
	lateinit var timeTableLogic: TimeTableLogic

	fun makeCalendarItem (startTime:Long, endTime:Long, allDay:Boolean = false):CalendarItem {
		return CalendarItem("mock",
			null,
			null,
			null,
			null,
			null,
			null,
			setOf(CodeStub("id")),
			setOf(CodeStub("id")),
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			null,
			startTime,
			endTime,
			null,
			null,
			15*60*1000,
			allDay,
			null,
			null,
			agendaId)
	}


	beforeEach {
		clearAllMocks()
		val agendaLogic = mockk<AgendaLogic>()
		coEvery { agendaLogic.getAgenda(any()) } answers { Agenda(id = firstArg()) }
		every { agendaLogic.getAgendasByUser(any()) } answers {
			flowOf(Agenda(id = agendaId, userId = firstArg()))
		}
		val calendarItemLogic = mockk<CalendarItemLogic>()
		every { calendarItemLogic.getCalendarItemByPeriodAndAgendaId(any(), any(), agendaId) } answers { flowOf(
			makeCalendarItem(20200102081500L, 20200102083000L),
			makeCalendarItem(20200102085500L, 20200102091000L),
			makeCalendarItem(20200110000000L, 20200110000000L, true)
		) }
		val calendarItemTypeLogic = mockk<CalendarItemTypeLogic>()
		every { calendarItemTypeLogic.getCalendarItemTypes(any()) } answers { firstArg<Collection<String>>().map { CalendarItemType(id = it, duration = 15) }.asFlow() }
		sessionMock = SessionMock()
		timeTableLogic = TimeTableLogicImpl(
			FakeTimeTableDAO(),
			agendaLogic,
			calendarItemTypeLogic,
			calendarItemLogic,
			sessionMock.sessionLogic
		)
	}


	suspend fun <T> withAuthenticatedHcpContext(hcpId: String, block: suspend () -> T): T =
		sessionMock.withAuthenticatedHcpContext(uri, groupId, hcpId, block)

	suspend fun TimeTable.create(): TimeTable {
		check(rev == null)
		return withAuthenticatedHcpContext(hcpId) {
			timeTableLogic.createTimeTable(this).shouldNotBeNull()
		}
	}

	suspend fun makeTimeTable(
		calendarItemTypeId: String,
		agendaId: String,
		rrule: String?,
		rruleStartDate: Long?,
		days: List<String>?,
		recurrenceTypes: List<String>?,
		acceptsNewPatient: Boolean = true,
		startHour:Long = 80000,
		endHour:Long = 170000,
	) {
		TimeTable(
			id = newId(),
			agendaId = agendaId,
			startTime = 20191015000000L,
			endTime = 20321006000000L,
			items = listOf(
				TimeTableItem (
					acceptsNewPatient = acceptsNewPatient,
					rruleStartDate = rruleStartDate,
					rrule = rrule,
					days = days ?: emptyList(),
					recurrenceTypes = recurrenceTypes ?: emptyList(),
					hours = listOf(
						TimeTableHour(
							startHour = startHour,
							endHour = endHour,
						)
					),
					calendarItemTypeId = calendarItemTypeId,
					publicTimeTableItem = true,
				)
			),
		).create()
	}

	"Rrule timetables should return results" {
		val calendarItemTypeId = newId()
		makeTimeTable(calendarItemTypeId, agendaId, "FREQ=WEEKLY;INTERVAL=1;UNTIL=20321006170000;BYDAY=SA,WE,TH,MO", 20221016L, null, null)
		withAuthenticatedHcpContext(hcpId) {
			val result = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20221016000000L, 20221118000000L, calendarItemTypeId, null, false, true, hcpId).toList()
			result.size shouldBe 100
			result[0] shouldBe 20221017080000L
			result shouldNotHaveElement { it % 1000000 < 80000 || it % 1000000 > 164500 }
		}
	}

	"Rrule timetables should return the same results even if rruleStartDate is moved earlier" {
		val calendarItemTypeId = newId()
		makeTimeTable(calendarItemTypeId, agendaId, "FREQ=WEEKLY;INTERVAL=1;UNTIL=20321006170000;BYDAY=SA,WE,TH,MO", 19991016L, null, null)
		withAuthenticatedHcpContext(hcpId) {
			val result = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20221016000000L, 20221118000000L, calendarItemTypeId, null, false, true, hcpId).toList()
			result.size shouldBe 100
			result[0] shouldBe 20221017080000L
			result shouldNotHaveElement { it % 1000000 < 80000 || it % 1000000 > 164500 }
		}
	}

	"Legacy timetables should return results" {
		val calendarItemTypeId = newId()

		makeTimeTable(calendarItemTypeId, agendaId, null, null, listOf("1", "3", "4", "6"), listOf("EVERY_WEEK"))
		withAuthenticatedHcpContext(hcpId) {
			val result = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20221016000000L, 20221118000000L, calendarItemTypeId, null, false, true, hcpId).toList()
			result.size shouldBe 100
			result[0] shouldBe 20221017080000L
			result shouldNotHaveElement { it % 1000000 < 80000 || it % 1000000 > 164500 }
		}
	}

	"Rrule and legacy time tables should return the same results" {
		val calendarItemTypeId1 = newId()
		val calendarItemTypeId2 = newId()

		makeTimeTable(calendarItemTypeId1, agendaId, "FREQ=WEEKLY;INTERVAL=1;UNTIL=20321006170000;BYDAY=SA,WE,TH,MO", null, null, null)
		makeTimeTable(calendarItemTypeId2, agendaId, null, null, listOf("1", "3", "4", "6"), listOf("EVERY_WEEK"))

		withAuthenticatedHcpContext(hcpId) {
			val result1 = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20221016000000L, 20221118000000L, calendarItemTypeId1, null, false, true, hcpId).toList()
			val result2 = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20221016000000L, 20221118000000L, calendarItemTypeId2, null, false, true, hcpId).toList()
			result1 shouldBe result2
		}
	}

	"Legacy should return different results with different inputs" {
		val calendarItemTypeId1 = newId()
		val calendarItemTypeId2 = newId()


		makeTimeTable(calendarItemTypeId1, agendaId, null, null, listOf("1", "3", "4", "6"), listOf("EVERY_WEEK"))
		makeTimeTable(calendarItemTypeId2, agendaId, null, null, listOf("1", "3", "4"), listOf("EVERY_WEEK"))

		withAuthenticatedHcpContext(hcpId) {
			val result1 = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20221016000000L, 20221118000000L, calendarItemTypeId1, null, false, true, hcpId, 200).toList()
			val result2 = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20221016000000L, 20221118000000L, calendarItemTypeId2, null, false, true, hcpId, 200).toList()
			result1 shouldNotBe result2 // fail: results are identical
		}
	}

	"Rrule timetable should return an empty array if rruleStartDate is set after rrule's endDate" {
		val calendarItemTypeId1 = newId()
		makeTimeTable(calendarItemTypeId1, agendaId, "FREQ=WEEKLY;INTERVAL=1;UNTIL=1999006170000;BYDAY=SA,WE,TH,MO",20221118L, null , null)

		withAuthenticatedHcpContext(hcpId) {
			val result1 = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20221016000000L, 20221118000000L, calendarItemTypeId1, null, false, true, hcpId).toList()
			result1 shouldBe listOf<Flow<Long>>()
		}
	}

	"Rrule timetable should return different results if rruleStartDate is different" {
		val calendarItemTypeId = newId()
		val oneWeekLaterCalendarItemTypeId = newId()

		makeTimeTable(calendarItemTypeId, agendaId, "FREQ=WEEKLY;INTERVAL=1;UNTIL=20321006170000;BYDAY=MO",20200101L, null , null)
		makeTimeTable(oneWeekLaterCalendarItemTypeId, agendaId, "FREQ=WEEKLY;INTERVAL=1;UNTIL=20321006170000;BYDAY=MO",20200107L, null , null)

		withAuthenticatedHcpContext(hcpId) {
			val result = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20200101000000L, 20401118000000L, calendarItemTypeId, null, false, true, hcpId).toList()
			val oneMWeekLater = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20200101000000L, 20401118000000L, oneWeekLaterCalendarItemTypeId, null, false, true, hcpId).toList()
			result[0] shouldBe 20200106080000L
			oneMWeekLater[0] shouldBe 20200113080000L
		}
	}

	"When startDate and endDate exactly match the same timeslot, it should only return the matching timeslot" {
		val calendarItemTypeId= newId()
		makeTimeTable(calendarItemTypeId, agendaId, "FREQ=WEEKLY;INTERVAL=1;UNTIL=20321006170000;BYDAY=MO",20200101L, null , null)
		withAuthenticatedHcpContext(hcpId) {
			val everyWeek = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20200106080000L, 20200106080000L, calendarItemTypeId, null, false, true, hcpId).toList()
			everyWeek [0] shouldBe 20200106080000L
			everyWeek.size shouldBe 1
		}
	}

	"When rrule is specified with a COUNT property instead of UNTIL, the number of results should match the specified count" {
		val calendarItemTypeId= newId()
		makeTimeTable(calendarItemTypeId, agendaId, "FREQ=DAILY;INTERVAL=1;COUNT=3",20200101L, null , null)
		withAuthenticatedHcpContext(hcpId) {
			val test = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20200101080000L, 20300106080000L, calendarItemTypeId, null, false, true, hcpId, 200).toList()
			test.size shouldBe 105 //9 hours / day * 4 quarters/hour * 3 days - 3 slots with existing cis
		}
	}

	"It should remove timeslots overlapping with existing calendarItems" {
		val calendarItemTypeId= newId()
		makeTimeTable(calendarItemTypeId, agendaId, "FREQ=DAILY;INTERVAL=1;UNTIL=20321006170000",20200101L, null , null)
		withAuthenticatedHcpContext(hcpId) {
			val result = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(calendarItemTypeId, 20200102080000L, 20200102100000L, calendarItemTypeId, null, false, true, hcpId).toList()
			result shouldBe listOf(20200102080000L,20200102083000L,20200102091500L,20200102093000L,20200102094500L,20200102100000L)
		}
	}

	"It should remove timeslots overlapping with existing calendarItems in the legacy format too" {
		val calendarItemTypeId= newId()
		makeTimeTable(calendarItemTypeId, agendaId, null,null, listOf("1", "2", "3", "4", "5", "6", "7" ) , listOf("EVERY_WEEK"))
		withAuthenticatedHcpContext(hcpId) {
			val result = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(calendarItemTypeId, 20200102080000L, 20200102100000L, calendarItemTypeId, null, false, true, hcpId).toList()
			result shouldBe listOf(20200102080000L,20200102083000L,20200102091500L,20200102093000L,20200102094500L,20200102100000L) //Fail:  Element 20200102080000L expected at index 0 but there were no further elements
		}
	}

	"It should return an empty list if the provided calendarItemType differs from the timetableItem's calendarItemTypes" {
		val calendarItemTypeId= newId()
		makeTimeTable(calendarItemTypeId, agendaId, "FREQ=DAILY;INTERVAL=1;UNTIL=20321006170000",20200101L, null , null)
		withAuthenticatedHcpContext(hcpId) {
			val result = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20200102080000L, 20200102100000L, "SOME OTHER CALENDARITEMTYPE ID", null, false, true, hcpId).toList()
			result.size shouldBe 0
		}
	}

	"It should return an empty list if isNewPatient is set to true and the timetableItem's acceptsNewPatient property is set to false." {
		val calendarItemTypeId= newId()
		makeTimeTable(calendarItemTypeId, agendaId, "FREQ=DAILY;INTERVAL=1;UNTIL=20321006170000",20200101L, null , null, false)
		withAuthenticatedHcpContext(hcpId) {
			val result = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20200102080000L, 20200102100000L, calendarItemTypeId, null, true, true, hcpId).toList()
			result.size shouldBe 0
		}
	}

	"There should not be any availability at days with an allDay-appointment" {
		val calendarItemTypeId= newId()
		makeTimeTable(calendarItemTypeId, agendaId, "FREQ=DAILY;INTERVAL=1;UNTIL=20321006170000",20200101L, null , null)
		withAuthenticatedHcpContext(hcpId) {
			val everyDay = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20200110080000L, 20200110100000L, calendarItemTypeId, null, false, true, hcpId).toList()
			everyDay.size shouldBe 0
		}
	}


	"In legacy format, time slots should match the start of the timetable + multiple of duration and NOT the time of the startDate" {
		val calendarItemTypeId= newId()
		makeTimeTable(calendarItemTypeId, agendaId, null, null, listOf("1", "2","3", "4","5","6", "7"), listOf("EVERY_WEEK"))
		withAuthenticatedHcpContext(hcpId) {
			val everyDay = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20200105080700L, 20200106080700L, calendarItemTypeId, null, false, true, hcpId).toList()
			everyDay[0] shouldBe 20200105081500L
		}
	}

	"In rrule format Timeslot time should be after the startDate" {
		val calendarItemTypeId= newId()
		makeTimeTable(calendarItemTypeId, agendaId, "FREQ=DAILY;INTERVAL=1;UNTIL=20321006170000",20200101L, null , null)
		withAuthenticatedHcpContext(hcpId) {
			val everyDay = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20200105080800L, 20200106080700L, calendarItemTypeId, null, false, true, hcpId).toList()
			everyDay[0] shouldBe 20200105081500L
		}
	}


	"In legacy and rrule version, it should return an empty array if the timeTableItem's startHour is equal or after the endHour" {
		val legacyCalendarItemTypeId = newId()
		val rruleCalendarItemTypeId = newId()
		makeTimeTable(legacyCalendarItemTypeId, agendaId, null, null, listOf("1", "2","3", "4","5","6", "7"), listOf("EVERY_WEEK"),true,100000,80000)
		makeTimeTable(rruleCalendarItemTypeId, agendaId, "FREQ=DAILY;INTERVAL=1;UNTIL=20321006170000",20200101L, null, null,true,100000,80000)
		withAuthenticatedHcpContext(hcpId) {
			val legacy = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20230120102441L, 20240120102441L, legacyCalendarItemTypeId, null, false, true, hcpId).toList()
			val rrule = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20230120102441L, 20240120102441L, rruleCalendarItemTypeId, null, false, true, hcpId).toList()
			legacy.size shouldBe (0) //expected:<0> but was:<100>
			rrule.size shouldBe (0) //expected:<0> but was:<100>
		}
	}


})

infix fun <E, T:Collection<E>> T.shouldNotHaveElement(test: (E) -> Boolean) = should(object:Matcher<T> {
	override fun test(value: T): MatcherResult {
		val result = value.any { test(it) }
		return MatcherResult(!result, "Collection should not have element matching $test", "Collection should have element matching $test")
	}
})
