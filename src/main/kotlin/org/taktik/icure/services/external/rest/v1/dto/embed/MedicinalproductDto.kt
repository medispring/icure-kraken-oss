/*
 * Copyright (C) 2018 Taktik SA
 *
 * This file is part of iCureBackend.
 *
 * iCureBackend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * iCureBackend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with iCureBackend.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.taktik.icure.services.external.rest.v1.dto.embed

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.services.external.rest.v1.dto.base.CodeStubDto
import org.taktik.icure.validation.AutoFix
import org.taktik.icure.validation.ValidCode
import java.io.Serializable

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class MedicinalproductDto(
        @field:ValidCode(autoFix = AutoFix.NORMALIZECODE)
        val intendedcds: List<CodeStubDto> = listOf(),

        @field:ValidCode(autoFix = AutoFix.NORMALIZECODE)
        val deliveredcds: List<CodeStubDto> = listOf(),
        val intendedname: String? = null,
        val deliveredname: String? = null,
        val productId: String? = null
) : Serializable
