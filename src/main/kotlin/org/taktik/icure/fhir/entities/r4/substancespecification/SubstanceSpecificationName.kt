//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.fhir.entities.r4.substancespecification

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.fhir.entities.r4.backboneelement.BackboneElement
import org.taktik.icure.fhir.entities.r4.codeableconcept.CodeableConcept
import org.taktik.icure.fhir.entities.r4.extension.Extension
import org.taktik.icure.fhir.entities.r4.reference.Reference

/**
 * Names applicable to this substance
 *
 * Names applicable to this substance.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class SubstanceSpecificationName(
  val domain: List<CodeableConcept> = listOf(),
  override val extension: List<Extension> = listOf(),
  /**
   * Unique id for inter-element referencing
   */
  override val id: String? = null,
  val jurisdiction: List<CodeableConcept> = listOf(),
  val language: List<CodeableConcept> = listOf(),
  override val modifierExtension: List<Extension> = listOf(),
  /**
   * The actual name
   */
  val name: String? = null,
  val official: List<SubstanceSpecificationNameOfficial> = listOf(),
  /**
   * If this is the preferred name for this substance
   */
  val preferred: Boolean? = null,
  val source: List<Reference> = listOf(),
  /**
   * The status of the name
   */
  val status: CodeableConcept? = null,
  val synonym: List<SubstanceSpecificationName> = listOf(),
  val translation: List<SubstanceSpecificationName> = listOf(),
  /**
   * Name type
   */
  val type: CodeableConcept? = null
) : BackboneElement
