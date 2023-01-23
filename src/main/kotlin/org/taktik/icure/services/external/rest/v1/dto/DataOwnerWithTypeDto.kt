package org.taktik.icure.services.external.rest.v1.dto

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer

@JsonSerialize(using = DataOwnerWithTypeSerializer::class)
sealed interface DataOwnerWithTypeDto {
	val dataOwner: Any

	data class HcpDataOwner(override val dataOwner: HealthcarePartyDto): DataOwnerWithTypeDto
	class PatientDataOwner(override val dataOwner: PatientDto): DataOwnerWithTypeDto
	class DeviceDataOwner(override val dataOwner: DeviceDto): DataOwnerWithTypeDto
}

class DataOwnerWithTypeSerializer: StdSerializer<DataOwnerWithTypeDto>(DataOwnerWithTypeDto::class.java) {
	override fun serialize(
		value: DataOwnerWithTypeDto,
		gen: JsonGenerator,
		provider: SerializerProvider
	) {
		gen.writeStartObject()
		gen.writeStringField(
			"type",
			when (value) {
				is DataOwnerWithTypeDto.DeviceDataOwner -> "device"
				is DataOwnerWithTypeDto.HcpDataOwner -> "hcp"
				is DataOwnerWithTypeDto.PatientDataOwner -> "patient"
			}
		)
		provider.defaultSerializeField("dataOwner", value.dataOwner, gen)
		gen.writeEndObject()
	}
}
