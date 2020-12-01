//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.fhir.entities.r4.substancepolymer

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.fhir.entities.r4.backboneelement.BackboneElement
import org.taktik.icure.fhir.entities.r4.codeableconcept.CodeableConcept
import org.taktik.icure.fhir.entities.r4.extension.Extension
import org.taktik.icure.fhir.entities.r4.substanceamount.SubstanceAmount

/**
 * Todo
 *
 * Todo.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class SubstancePolymerRepeatRepeatUnit(
  /**
   * Todo
   */
  val amount: SubstanceAmount? = null,
  val degreeOfPolymerisation: List<SubstancePolymerRepeatRepeatUnitDegreeOfPolymerisation> =
      listOf(),
  override val extension: List<Extension> = listOf(),
  /**
   * Unique id for inter-element referencing
   */
  override val id: String? = null,
  override val modifierExtension: List<Extension> = listOf(),
  /**
   * Todo
   */
  val orientationOfPolymerisation: CodeableConcept? = null,
  /**
   * Todo
   */
  val repeatUnit: String? = null,
  val structuralRepresentation: List<SubstancePolymerRepeatRepeatUnitStructuralRepresentation> =
      listOf()
) : BackboneElement
