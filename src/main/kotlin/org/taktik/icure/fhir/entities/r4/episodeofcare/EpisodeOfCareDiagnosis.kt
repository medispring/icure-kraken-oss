//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.fhir.entities.r4.episodeofcare

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.fhir.entities.r4.backboneelement.BackboneElement
import org.taktik.icure.fhir.entities.r4.codeableconcept.CodeableConcept
import org.taktik.icure.fhir.entities.r4.extension.Extension
import org.taktik.icure.fhir.entities.r4.reference.Reference

/**
 * The list of diagnosis relevant to this episode of care
 *
 * The list of diagnosis relevant to this episode of care.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class EpisodeOfCareDiagnosis(
  /**
   * Conditions/problems/diagnoses this episode of care is for
   */
  val condition: Reference,
  override val extension: List<Extension> = listOf(),
  /**
   * Unique id for inter-element referencing
   */
  override val id: String? = null,
  override val modifierExtension: List<Extension> = listOf(),
  /**
   * Ranking of the diagnosis (for each role type)
   */
  val rank: Int? = null,
  /**
   * Role that this diagnosis has within the episode of care (e.g. admission, billing, discharge …)
   */
  val role: CodeableConcept? = null
) : BackboneElement
