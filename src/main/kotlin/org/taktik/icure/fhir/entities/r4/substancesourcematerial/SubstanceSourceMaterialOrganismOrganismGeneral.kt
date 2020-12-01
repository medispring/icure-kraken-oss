//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.fhir.entities.r4.substancesourcematerial

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.pozo.KotlinBuilder
import org.taktik.icure.fhir.entities.r4.backboneelement.BackboneElement
import org.taktik.icure.fhir.entities.r4.codeableconcept.CodeableConcept
import org.taktik.icure.fhir.entities.r4.extension.Extension

/**
 * 4.9.13.7.1 Kingdom (Conditional)
 *
 * 4.9.13.7.1 Kingdom (Conditional).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class SubstanceSourceMaterialOrganismOrganismGeneral(
  /**
   * The class of an organism shall be specified
   */
  @JsonProperty("class")
  val class_fhir: CodeableConcept? = null,
  override val extension: List<Extension> = listOf(),
  /**
   * Unique id for inter-element referencing
   */
  override val id: String? = null,
  /**
   * The kingdom of an organism shall be specified
   */
  val kingdom: CodeableConcept? = null,
  override val modifierExtension: List<Extension> = listOf(),
  /**
   * The order of an organism shall be specified,
   */
  val order: CodeableConcept? = null,
  /**
   * The phylum of an organism shall be specified
   */
  val phylum: CodeableConcept? = null
) : BackboneElement
