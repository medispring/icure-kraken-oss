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

package org.taktik.icure.services.external.rest.v1.controllers.extra

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.taktik.couchdb.DocIdentifier
import org.taktik.icure.asynclogic.CalendarItemLogic
import org.taktik.icure.db.PaginationOffset
import org.taktik.icure.entities.CalendarItem
import org.taktik.icure.services.external.rest.v1.dto.CalendarItemDto
import org.taktik.icure.services.external.rest.v1.dto.IcureStubDto
import org.taktik.icure.services.external.rest.v1.dto.ListOfIdsDto
import org.taktik.icure.services.external.rest.v1.mapper.CalendarItemMapper
import org.taktik.icure.services.external.rest.v1.mapper.embed.DelegationMapper
import org.taktik.icure.services.external.rest.v1.utils.paginatedList
import org.taktik.icure.utils.injectReactorContext
import reactor.core.publisher.Flux

@ExperimentalCoroutinesApi
@RestController
@RequestMapping("/rest/v1/calendarItem")
@Tag(name = "calendarItem")
class CalendarItemController(
	private val calendarItemLogic: CalendarItemLogic,
	private val calendarItemMapper: CalendarItemMapper,
	private val delegationMapper: DelegationMapper,
	private val objectMapper: ObjectMapper,
) {

	private val calendarItemToCalendarItemDto = { it: CalendarItem -> calendarItemMapper.map(it) }

	@Operation(summary = "Gets all calendarItems")
	@GetMapping
	fun getCalendarItems(): Flux<CalendarItemDto> {
		val calendarItems = calendarItemLogic.getEntities()
		return calendarItems.map { calendarItemMapper.map(it) }.injectReactorContext()
	}

	@Operation(summary = "Creates a calendarItem")
	@PostMapping
	fun createCalendarItem(@RequestBody calendarItemDto: CalendarItemDto) = mono {
		val calendarItem = calendarItemLogic.createCalendarItem(calendarItemMapper.map(calendarItemDto))
			?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "CalendarItem creation failed")

		calendarItemMapper.map(calendarItem)
	}

	@Operation(summary = "Deletes calendarItems")
	@PostMapping("/delete/byIds")
	fun deleteCalendarItems(@RequestBody calendarItemIds: ListOfIdsDto): Flux<DocIdentifier> =
		calendarItemLogic.deleteCalendarItems(calendarItemIds.ids).injectReactorContext()

	@Deprecated(message = "Use deleteItemCalendars instead")
	@Operation(summary = "Deletes an calendarItem")
	@DeleteMapping("/{calendarItemIds}")
	fun deleteCalendarItem(@PathVariable calendarItemIds: String): Flux<DocIdentifier> =
		calendarItemLogic.deleteCalendarItems(calendarItemIds.split(',')).injectReactorContext()

	@Operation(summary = "Gets an calendarItem")
	@GetMapping("/{calendarItemId}")
	fun getCalendarItem(@PathVariable calendarItemId: String) = mono {
		val calendarItem = calendarItemLogic.getCalendarItem(calendarItemId)
			?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "CalendarItem fetching failed")

		calendarItemMapper.map(calendarItem)
	}

	@Operation(summary = "Modifies an calendarItem")
	@PutMapping
	fun modifyCalendarItem(@RequestBody calendarItemDto: CalendarItemDto) = mono {
		val calendarItem = calendarItemLogic.modifyCalendarItem(calendarItemMapper.map(calendarItemDto))
			?: throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "CalendarItem modification failed")

		calendarItemMapper.map(calendarItem)
	}

	@Operation(summary = "Get CalendarItems by Period and HcPartyId")
	@PostMapping("/byPeriodAndHcPartyId")
	fun getCalendarItemsByPeriodAndHcPartyId(
		@RequestParam startDate: Long,
		@RequestParam endDate: Long,
		@RequestParam hcPartyId: String
	): Flux<CalendarItemDto> {
		if (hcPartyId.isBlank()) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "hcPartyId was empty")
		}
		val calendars = calendarItemLogic.getCalendarItemByPeriodAndHcPartyId(startDate, endDate, hcPartyId)
		return calendars.map { calendarItemMapper.map(it) }.injectReactorContext()
	}

	@Operation(summary = "Get CalendarItems by Period and AgendaId")
	@PostMapping("/byPeriodAndAgendaId")
	fun getCalendarsByPeriodAndAgendaId(
		@RequestParam startDate: Long,
		@RequestParam endDate: Long,
		@RequestParam agendaId: String
	): Flux<CalendarItemDto> {
		if (agendaId.isBlank()) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "agendaId was empty")
		}
		val calendars = calendarItemLogic.getCalendarItemByPeriodAndAgendaId(startDate, endDate, agendaId)
		return calendars.map { calendarItemMapper.map(it) }.injectReactorContext()
	}

	@Operation(summary = "Get calendarItems by ids")
	@PostMapping("/byIds")
	fun getCalendarItemsWithIds(@RequestBody calendarItemIds: ListOfIdsDto?): Flux<CalendarItemDto> {
		if (calendarItemIds == null) {
			throw ResponseStatusException(HttpStatus.BAD_REQUEST, "calendarItemIds was empty")
		}
		val calendars = calendarItemLogic.getCalendarItems(calendarItemIds.ids)
		return calendars.map { calendarItemMapper.map(it) }.injectReactorContext()
	}

	@Operation(summary = "Find CalendarItems by hcparty and patient", description = "")
	@GetMapping("/byHcPartySecretForeignKeys")
	fun listCalendarItemsByHCPartyPatientForeignKeys(
		@RequestParam hcPartyId: String,
		@RequestParam secretFKeys: String
	): Flux<CalendarItemDto> {
		val secretPatientKeys = secretFKeys.split(',').map { it.trim() }
		val elementList = calendarItemLogic.listCalendarItemsByHCPartyAndSecretPatientKeys(hcPartyId, secretPatientKeys)

		return elementList.map { calendarItemMapper.map(it) }.injectReactorContext()
	}

	@Operation(summary = "Find CalendarItems by hcparty and patient")
	@PostMapping("/byHcPartySecretForeignKeys")
	fun listCalendarItemsByHCPartyPatientForeignKeys(
		@RequestParam hcPartyId: String,
		@RequestBody secretPatientKeys: List<String>
	): Flux<CalendarItemDto> {
		val elementList = calendarItemLogic.listCalendarItemsByHCPartyAndSecretPatientKeys(hcPartyId, secretPatientKeys)

		return elementList.map { calendarItemMapper.map(it) }.injectReactorContext()
	}

	@Operation(summary = "Find CalendarItems by hcparty and patient", description = "")
	@GetMapping("/byHcPartySecretForeignKeys/page/{limit}")
	fun findCalendarItemsByHCPartyPatientForeignKeys(
		@RequestParam hcPartyId: String,
		@RequestParam secretFKeys: String,
		@Parameter(description = "The start key for pagination: a JSON representation of an array containing all the necessary " + "components to form the Complex Key's startKey") @RequestParam(required = false) startKey: String?,
		@Parameter(description = "A patient document ID") @RequestParam(required = false) startDocumentId: String?,
		@Parameter(description = "Number of rows") @PathVariable limit: Int,
	) = mono {
		val secretPatientKeys = secretFKeys.split(',').map { it.trim() }
		val startKeyElements = startKey?.let { objectMapper.readValue<List<Any>>(startKey) }
		val paginationOffset = PaginationOffset(startKeyElements, startDocumentId, null, limit + 1)
		val elementList = calendarItemLogic.findCalendarItemsByHCPartyAndSecretPatientKeys(hcPartyId, secretPatientKeys, paginationOffset)

		elementList.paginatedList(calendarItemToCalendarItemDto, limit)
	}

	@Operation(summary = "Find CalendarItems by hcparty and patient")
	@PostMapping("/byHcPartySecretForeignKeys/page/{limit}")
	fun findCalendarItemsByHCPartyPatientForeignKeys(
		@RequestParam hcPartyId: String,
		@RequestBody secretPatientKeys: List<String>,
		@Parameter(description = "The start key for pagination: a JSON representation of an array containing all the necessary " + "components to form the Complex Key's startKey") @RequestParam(required = false) startKey: String?,
		@Parameter(description = "A patient document ID") @RequestParam(required = false) startDocumentId: String?,
		@Parameter(description = "Number of rows") @PathVariable(required = false) limit: Int,
	) = mono {
		val startKeyElements = startKey?.let { objectMapper.readValue<List<Any>>(startKey) }
		val paginationOffset = PaginationOffset(startKeyElements, startDocumentId, null, limit + 1)
		val elementList = calendarItemLogic.findCalendarItemsByHCPartyAndSecretPatientKeys(hcPartyId, secretPatientKeys, paginationOffset)

		elementList.paginatedList(calendarItemToCalendarItemDto, limit)
	}

	@Operation(summary = "Update delegations in calendarItems")
	@PostMapping("/delegations")
	fun setCalendarItemsDelegations(@RequestBody stubs: List<IcureStubDto>) = flow {
		val calendarItems = calendarItemLogic.getCalendarItems(stubs.map { obj: IcureStubDto -> obj.id }).map { ci ->
			stubs.find { s -> s.id == ci.id }?.let { stub ->
				ci.copy(
					delegations = ci.delegations.mapValues { (s, dels) -> stub.delegations[s]?.map { delegationMapper.map(it) }?.toSet() ?: dels } +
						stub.delegations.filterKeys { k -> !ci.delegations.containsKey(k) }.mapValues { (_, value) -> value.map { delegationMapper.map(it) }.toSet() },
					encryptionKeys = ci.encryptionKeys.mapValues { (s, dels) -> stub.encryptionKeys[s]?.map { delegationMapper.map(it) }?.toSet() ?: dels } +
						stub.encryptionKeys.filterKeys { k -> !ci.encryptionKeys.containsKey(k) }.mapValues { (_, value) -> value.map { delegationMapper.map(it) }.toSet() },
					cryptedForeignKeys = ci.cryptedForeignKeys.mapValues { (s, dels) -> stub.cryptedForeignKeys[s]?.map { delegationMapper.map(it) }?.toSet() ?: dels } +
						stub.cryptedForeignKeys.filterKeys { k -> !ci.cryptedForeignKeys.containsKey(k) }.mapValues { (_, value) -> value.map { delegationMapper.map(it) }.toSet() },
				)
			} ?: ci
		}
		emitAll(calendarItemLogic.modifyEntities(calendarItems.toList()).map { calendarItemMapper.map(it) })
	}.injectReactorContext()

	@Operation(summary = "Find CalendarItems by recurrenceId", description = "")
	@GetMapping("/byRecurrenceId")
	fun findCalendarItemsByRecurrenceId(@RequestParam recurrenceId: String): Flux<CalendarItemDto> {
		val elementList = calendarItemLogic.getCalendarItemsByRecurrenceId(recurrenceId)
		return elementList.map { calendarItemMapper.map(it) }.injectReactorContext()
	}
}
