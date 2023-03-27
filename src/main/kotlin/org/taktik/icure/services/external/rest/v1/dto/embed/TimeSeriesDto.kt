package org.taktik.icure.services.external.rest.v1.dto.embed

import java.io.Serializable
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class TimeSeriesDto(
	val fields: List<String> = emptyList(),
	val samples: List<List<Double>> = emptyList(),
	val min: List<Double> = emptyList(),
	val max: List<Double> = emptyList(),
	val mean: List<Double> = emptyList(),
	val median: List<Double> = emptyList(),
	val variance: List<Double> = emptyList(),
): Serializable
