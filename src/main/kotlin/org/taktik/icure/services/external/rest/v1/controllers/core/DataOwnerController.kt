package org.taktik.icure.services.external.rest.v1.controllers.core

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.mono
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import org.taktik.icure.asynclogic.AsyncSessionLogic
import org.taktik.icure.asynclogic.DeviceLogic
import org.taktik.icure.asynclogic.HealthcarePartyLogic
import org.taktik.icure.asynclogic.PatientLogic
import org.taktik.icure.asynclogic.UserLogic
import org.taktik.icure.services.external.rest.v1.dto.DataOwnerWithTypeDto
import org.taktik.icure.services.external.rest.v1.mapper.DeviceMapper
import org.taktik.icure.services.external.rest.v1.mapper.HealthcarePartyMapper
import org.taktik.icure.services.external.rest.v1.mapper.PatientMapper

@ExperimentalCoroutinesApi
@RestController
@RequestMapping("/rest/v1/dataowner")
@Tag(name = "dataowner")
class DataOwnerController(
	private val patientLogic: PatientLogic,
	private val patientMapper: PatientMapper,
	private val deviceLogic: DeviceLogic,
	private val deviceMapper: DeviceMapper,
	private val hcpLogic: HealthcarePartyLogic,
	private val healthcarePartyMapper: HealthcarePartyMapper,
	private val userLogic: UserLogic,
	private val sessionLogic: AsyncSessionLogic
) {

	@Operation(summary = "Get a data owner by his ID", description = "General information about the data owner")
	@GetMapping("/{dataOwnerId}")
	fun getDataOwner(@PathVariable dataOwnerId: String) = mono {
		kotlin.runCatching { patientLogic.getPatient(dataOwnerId) }.getOrNull()?.let {
			DataOwnerWithTypeDto.PatientDataOwner(patientMapper.map(it))
		} ?: kotlin.runCatching { hcpLogic.getHealthcareParty(dataOwnerId) }.getOrNull()?.let {
			DataOwnerWithTypeDto.HcpDataOwner(healthcarePartyMapper.map(it))
		} ?: kotlin.runCatching { deviceLogic.getDevice(dataOwnerId) }.getOrNull()?.let {
			DataOwnerWithTypeDto.DeviceDataOwner(deviceMapper.map(it))
		} ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find any data owner with id $dataOwnerId")
	}

	@Operation(summary = "Get the data owner corresponding to the current user", description = "General information about the current data owner")
	@GetMapping("/current")
	fun getCurrentDataOwner() = mono {
		val user = userLogic.getUser(sessionLogic.getCurrentUserId())
			?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Getting Current User failed. Possible reasons: no such user exists, or server error. Please try again or read the server log.")
		(user.healthcarePartyId ?: user.patientId ?: user.deviceId)?.let { getDataOwner(it) }?.awaitSingle()
			?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Could not find any data owner associated to the current user.")
	}
}
