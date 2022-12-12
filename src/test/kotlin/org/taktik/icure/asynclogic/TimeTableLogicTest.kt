package org.taktik.icure.asynclogic

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
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
import org.taktik.icure.entities.CalendarItemType
import org.taktik.icure.entities.TimeTable
import org.taktik.icure.entities.embed.TimeTableHour
import org.taktik.icure.entities.embed.TimeTableItem
import org.taktik.icure.test.SessionMock
import org.taktik.icure.test.fakedaos.FakeTimeTableDAO
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

	beforeEach {
		clearAllMocks()
		val agendaLogic = mockk<AgendaLogic>()
		coEvery { agendaLogic.getAgenda(any()) } answers { Agenda(id = firstArg()) }
		every { agendaLogic.getAgendasByUser(any()) } answers {
			flowOf(Agenda(id = agendaId, userId = firstArg()))
		}
		val calendarItemLogic = mockk<CalendarItemLogic>()
		every { calendarItemLogic.getCalendarItemByPeriodAndAgendaId(any(), any(), agendaId) } answers { flowOf() }
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
		recurrenceTypes: List<String>?
	) {
		TimeTable(
			id = newId(),
			agendaId = agendaId,
			startTime = 20221015000000L,
			endTime = 20321006000000L,
			items = listOf(
				TimeTableItem (
					rruleStartDate = rruleStartDate,
					rrule = rrule,
					days = days ?: emptyList(),
					recurrenceTypes = recurrenceTypes ?: emptyList(),
					hours = listOf(
						TimeTableHour(
							startHour = 80000,
							endHour = 170000,
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
		makeTimeTable(calendarItemTypeId, agendaId, "FREQ=WEEKLY;INTERVAL=1;UNTIL=20321006170000;BYDAY=SA,WE,TH,MO", 20221016000000L, null, null)
		withAuthenticatedHcpContext(hcpId) {
			val result = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20221016000000L, 20221118000000L, calendarItemTypeId, null, false, true, hcpId).toList()
			result.size shouldBe 100
			result[0] shouldBe 20221017080000L
			result shouldNotHaveElement { it % 1000000 < 80000 || it % 1000000 > 164500 }
		}
	}

	"Rrule timetables should return the same results even if recurrenceStartDate is moved earlier" {
		val calendarItemTypeId = newId()
		makeTimeTable(calendarItemTypeId, agendaId, "FREQ=WEEKLY;INTERVAL=1;UNTIL=20321006170000;BYDAY=SA,WE,TH,MO", 19991016000000L, null, null)
		withAuthenticatedHcpContext(hcpId) {
			val result = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20221016000000L, 20221118000000L, calendarItemTypeId, null, false, true, hcpId).toList()
			result.size shouldBe 100
			result[0] shouldBe 20221017080000L //FAIL expected:<20221017080000L> but was:<19991016080000L>
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
			val result2 = timeTableLogic.getAvailabilitiesByPeriodAndCalendarItemTypeId(newId(), 20221016000000L, 20221118000000L, calendarItemTypeId1, null, false, true, hcpId).toList()
			result1 shouldBe result2
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
})

infix fun <E, T:Collection<E>> T.shouldNotHaveElement(test: (E) -> Boolean) = should(object:Matcher<T> {
	override fun test(value: T): MatcherResult {
		val result = value.any { test(it) }
		return MatcherResult(!result, "Collection should not have element matching $test", "Collection should have element matching $test")
	}
})
