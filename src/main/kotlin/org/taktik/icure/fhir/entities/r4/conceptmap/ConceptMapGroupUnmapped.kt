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

//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.fhir.entities.r4.conceptmap

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.fhir.entities.r4.backboneelement.BackboneElement
import org.taktik.icure.fhir.entities.r4.extension.Extension

/**
 * What to do when there is no mapping for the source concept
 *
 * What to do when there is no mapping for the source concept. "Unmapped" does not include codes
 * that are unmatched, and the unmapped element is ignored in a code is specified to have equivalence =
 * unmatched.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class ConceptMapGroupUnmapped(
  /**
   * Fixed code when mode = fixed
   */
  val code: String? = null,
  /**
   * Display for the code
   */
  val display: String? = null,
  override val extension: List<Extension> = listOf(),
  /**
   * Unique id for inter-element referencing
   */
  override val id: String? = null,
  /**
   * provided | fixed | other-map
   */
  val mode: String? = null,
  override val modifierExtension: List<Extension> = listOf(),
  /**
   * canonical reference to an additional ConceptMap to use for mapping if the source concept is
   * unmapped
   */
  val url: String? = null
) : BackboneElement
