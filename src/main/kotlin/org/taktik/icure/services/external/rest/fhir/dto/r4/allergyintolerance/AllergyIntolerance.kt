//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.services.external.rest.fhir.dto.r4.allergyintolerance

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.services.external.rest.fhir.dto.r4.DomainResource
import org.taktik.icure.services.external.rest.fhir.dto.r4.Meta
import org.taktik.icure.services.external.rest.fhir.dto.r4.Resource
import org.taktik.icure.services.external.rest.fhir.dto.r4.age.Age
import org.taktik.icure.services.external.rest.fhir.dto.r4.annotation.Annotation
import org.taktik.icure.services.external.rest.fhir.dto.r4.codeableconcept.CodeableConcept
import org.taktik.icure.services.external.rest.fhir.dto.r4.extension.Extension
import org.taktik.icure.services.external.rest.fhir.dto.r4.identifier.Identifier
import org.taktik.icure.services.external.rest.fhir.dto.r4.narrative.Narrative
import org.taktik.icure.services.external.rest.fhir.dto.r4.period.Period
import org.taktik.icure.services.external.rest.fhir.dto.r4.range.Range
import org.taktik.icure.services.external.rest.fhir.dto.r4.reference.Reference

/**
 * Allergy or Intolerance (generally: Risk of adverse reaction to a substance)
 *
 * Risk of harmful or undesirable, physiological response which is unique to an individual and
 * associated with exposure to a substance.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class AllergyIntolerance(
  /**
   * Source of the information about the allergy
   */
  val asserter: Reference? = null,
  val category: List<String> = listOf(),
  /**
   * active | inactive | resolved
   */
  val clinicalStatus: CodeableConcept? = null,
  /**
   * Code that identifies the allergy or intolerance
   */
  val code: CodeableConcept? = null,
  override val contained: List<Resource> = listOf(),
  /**
   * low | high | unable-to-assess
   */
  val criticality: String? = null,
  /**
   * Encounter when the allergy or intolerance was asserted
   */
  val encounter: Reference? = null,
  override val extension: List<Extension> = listOf(),
  /**
   * Logical id of this artifact
   */
  override val id: String? = null,
  val identifier: List<Identifier> = listOf(),
  /**
   * A set of rules under which this content was created
   */
  override val implicitRules: String? = null,
  /**
   * Language of the resource content
   */
  override val language: String? = null,
  /**
   * Date(/time) of last known occurrence of a reaction
   */
  val lastOccurrence: String? = null,
  /**
   * Metadata about the resource
   */
  override val meta: Meta? = null,
  override val modifierExtension: List<Extension> = listOf(),
  val note: List<Annotation> = listOf(),
  /**
   * When allergy or intolerance was identified
   */
  val onsetAge: Age? = null,
  /**
   * When allergy or intolerance was identified
   */
  val onsetDateTime: String? = null,
  /**
   * When allergy or intolerance was identified
   */
  val onsetPeriod: Period? = null,
  /**
   * When allergy or intolerance was identified
   */
  val onsetRange: Range? = null,
  /**
   * When allergy or intolerance was identified
   */
  val onsetString: String? = null,
  /**
   * Who the sensitivity is for
   */
  val patient: Reference,
  val reaction: List<AllergyIntoleranceReaction> = listOf(),
  /**
   * Date first version of the resource instance was recorded
   */
  val recordedDate: String? = null,
  /**
   * Who recorded the sensitivity
   */
  val recorder: Reference? = null,
  /**
   * Text summary of the resource, for human interpretation
   */
  override val text: Narrative? = null,
  /**
   * allergy | intolerance - Underlying mechanism (if known)
   */
  val type: String? = null,
  /**
   * unconfirmed | confirmed | refuted | entered-in-error
   */
  val verificationStatus: CodeableConcept? = null
) : DomainResource
