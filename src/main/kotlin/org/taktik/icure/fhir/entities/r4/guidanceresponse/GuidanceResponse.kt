//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.fhir.entities.r4.guidanceresponse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.fhir.entities.r4.DomainResource
import org.taktik.icure.fhir.entities.r4.Meta
import org.taktik.icure.fhir.entities.r4.Resource
import org.taktik.icure.fhir.entities.r4.annotation.Annotation
import org.taktik.icure.fhir.entities.r4.codeableconcept.CodeableConcept
import org.taktik.icure.fhir.entities.r4.datarequirement.DataRequirement
import org.taktik.icure.fhir.entities.r4.extension.Extension
import org.taktik.icure.fhir.entities.r4.identifier.Identifier
import org.taktik.icure.fhir.entities.r4.narrative.Narrative
import org.taktik.icure.fhir.entities.r4.reference.Reference

/**
 * The formal response to a guidance request
 *
 * A guidance response is the formal response to a guidance request, including any output parameters
 * returned by the evaluation, as well as the description of any proposed actions to be taken.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class GuidanceResponse(
  override val contained: List<Resource> = listOf(),
  val dataRequirement: List<DataRequirement> = listOf(),
  /**
   * Encounter during which the response was returned
   */
  val encounter: Reference? = null,
  val evaluationMessage: List<Reference> = listOf(),
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
   * Metadata about the resource
   */
  override val meta: Meta? = null,
  override val modifierExtension: List<Extension> = listOf(),
  /**
   * What guidance was requested
   */
  val moduleCanonical: String? = null,
  /**
   * What guidance was requested
   */
  val moduleCodeableConcept: CodeableConcept,
  /**
   * What guidance was requested
   */
  val moduleUri: String? = null,
  val note: List<Annotation> = listOf(),
  /**
   * When the guidance response was processed
   */
  val occurrenceDateTime: String? = null,
  /**
   * The output parameters of the evaluation, if any
   */
  val outputParameters: Reference? = null,
  /**
   * Device returning the guidance
   */
  val performer: Reference? = null,
  val reasonCode: List<CodeableConcept> = listOf(),
  val reasonReference: List<Reference> = listOf(),
  /**
   * The identifier of the request associated with this response, if any
   */
  val requestIdentifier: Identifier? = null,
  /**
   * Proposed actions, if any
   */
  val result: Reference? = null,
  /**
   * success | data-requested | data-required | in-progress | failure | entered-in-error
   */
  val status: String? = null,
  /**
   * Patient the request was performed for
   */
  val subject: Reference? = null,
  /**
   * Text summary of the resource, for human interpretation
   */
  override val text: Narrative? = null
) : DomainResource
