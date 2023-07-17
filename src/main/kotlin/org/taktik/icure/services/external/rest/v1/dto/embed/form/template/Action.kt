package org.taktik.icure.services.external.rest.v1.dto.embed.form.template

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class Action(
	val launchers: List<Launcher>? = emptyList(),
	val expression: String? = null,
	val states : List<State>? = emptyList()
)
