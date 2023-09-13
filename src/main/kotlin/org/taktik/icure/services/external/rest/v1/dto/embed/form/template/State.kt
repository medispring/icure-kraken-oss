package org.taktik.icure.services.external.rest.v1.dto.embed.form.template

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
class State (
	val name: String = "",
	val stateToUpdate: StateToUpdate = StateToUpdate.VISIBLE,
	val canLaunchLauncher: Boolean = false,
)

enum class StateToUpdate {
	VALUE, VISIBLE, READONLY, CLAZZ, REQUIRED
}
