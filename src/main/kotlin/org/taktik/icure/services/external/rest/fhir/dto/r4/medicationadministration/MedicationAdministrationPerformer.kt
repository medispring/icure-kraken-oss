//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.services.external.rest.fhir.dto.r4.medicationadministration

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.services.external.rest.fhir.dto.r4.backboneelement.BackboneElement
import org.taktik.icure.services.external.rest.fhir.dto.r4.codeableconcept.CodeableConcept
import org.taktik.icure.services.external.rest.fhir.dto.r4.extension.Extension
import org.taktik.icure.services.external.rest.fhir.dto.r4.reference.Reference

/**
 * Who performed the medication administration and what they did
 *
 * Indicates who or what performed the medication administration and how they were involved.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class MedicationAdministrationPerformer(
  /**
   * Who performed the medication administration
   */
  val actor: Reference,
  override val extension: List<Extension> = listOf(),
  /**
   * Type of performance
   */
  val function: CodeableConcept? = null,
  /**
   * Unique id for inter-element referencing
   */
  override val id: String? = null,
  override val modifierExtension: List<Extension> = listOf()
) : BackboneElement
