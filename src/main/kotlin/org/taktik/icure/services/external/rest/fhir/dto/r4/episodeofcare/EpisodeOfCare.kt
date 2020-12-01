//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.services.external.rest.fhir.dto.r4.episodeofcare

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.services.external.rest.fhir.dto.r4.DomainResource
import org.taktik.icure.services.external.rest.fhir.dto.r4.Meta
import org.taktik.icure.services.external.rest.fhir.dto.r4.Resource
import org.taktik.icure.services.external.rest.fhir.dto.r4.codeableconcept.CodeableConcept
import org.taktik.icure.services.external.rest.fhir.dto.r4.extension.Extension
import org.taktik.icure.services.external.rest.fhir.dto.r4.identifier.Identifier
import org.taktik.icure.services.external.rest.fhir.dto.r4.narrative.Narrative
import org.taktik.icure.services.external.rest.fhir.dto.r4.period.Period
import org.taktik.icure.services.external.rest.fhir.dto.r4.reference.Reference

/**
 * An association of a Patient with an Organization and  Healthcare Provider(s) for a period of time
 * that the Organization assumes some level of responsibility
 *
 * An association between a patient and an organization / healthcare provider(s) during which time
 * encounters may occur. The managing organization assumes a level of responsibility for the patient
 * during this time.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class EpisodeOfCare(
  val account: List<Reference> = listOf(),
  /**
   * Care manager/care coordinator for the patient
   */
  val careManager: Reference? = null,
  override val contained: List<Resource> = listOf(),
  val diagnosis: List<EpisodeOfCareDiagnosis> = listOf(),
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
   * Organization that assumes care
   */
  val managingOrganization: Reference? = null,
  /**
   * Metadata about the resource
   */
  override val meta: Meta? = null,
  override val modifierExtension: List<Extension> = listOf(),
  /**
   * The patient who is the focus of this episode of care
   */
  val patient: Reference,
  /**
   * Interval during responsibility is assumed
   */
  val period: Period? = null,
  val referralRequest: List<Reference> = listOf(),
  /**
   * planned | waitlist | active | onhold | finished | cancelled | entered-in-error
   */
  val status: String? = null,
  val statusHistory: List<EpisodeOfCareStatusHistory> = listOf(),
  val team: List<Reference> = listOf(),
  /**
   * Text summary of the resource, for human interpretation
   */
  override val text: Narrative? = null,
  val type: List<CodeableConcept> = listOf()
) : DomainResource
