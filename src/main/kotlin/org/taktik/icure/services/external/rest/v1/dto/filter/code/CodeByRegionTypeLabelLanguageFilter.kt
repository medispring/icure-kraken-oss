package org.taktik.icure.services.external.rest.v1.dto.filter.code

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.github.pozo.KotlinBuilder
import org.taktik.icure.entities.base.Code
import org.taktik.icure.handlers.JsonPolymorphismRoot
import org.taktik.icure.services.external.rest.v1.dto.filter.AbstractFilterDto

@JsonPolymorphismRoot(AbstractFilterDto::class)
@JsonDeserialize(using = JsonDeserializer.None::class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class CodeByRegionTypeLabelLanguageFilter(
        override val desc:String? = null,
        override val region: String? = null,
        override val type: String? = null,
        override val language: String? = null,
        override val label: String? = null
) : AbstractFilterDto<Code>, org.taktik.icure.domain.filter.code.CodeByRegionTypeLabelLanguageFilter
