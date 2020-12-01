//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.fhir.entities.r4.requestgroup

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.fhir.entities.r4.DomainResource
import org.taktik.icure.fhir.entities.r4.Meta
import org.taktik.icure.fhir.entities.r4.Resource
import org.taktik.icure.fhir.entities.r4.annotation.Annotation
import org.taktik.icure.fhir.entities.r4.codeableconcept.CodeableConcept
import org.taktik.icure.fhir.entities.r4.extension.Extension
import org.taktik.icure.fhir.entities.r4.identifier.Identifier
import org.taktik.icure.fhir.entities.r4.narrative.Narrative
import org.taktik.icure.fhir.entities.r4.reference.Reference

/**
 * A group of related requests
 *
 * A group of related requests that can be used to capture intended activities that have
 * inter-dependencies such as "give this medication after that one".
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class RequestGroup(
  val action: List<RequestGroupAction> = listOf(),
  /**
   * Device or practitioner that authored the request group
   */
  val author: Reference? = null,
  /**
   * When the request group was authored
   */
  val authoredOn: String? = null,
  val basedOn: List<Reference> = listOf(),
  /**
   * What's being requested/ordered
   */
  val code: CodeableConcept? = null,
  override val contained: List<Resource> = listOf(),
  /**
   * Created as part of
   */
  val encounter: Reference? = null,
  override val extension: List<Extension> = listOf(),
  /**
   * Composite request this is part of
   */
  val groupIdentifier: Identifier? = null,
  /**
   * Logical id of this artifact
   */
  override val id: String? = null,
  val identifier: List<Identifier> = listOf(),
  /**
   * A set of rules under which this content was created
   */
  override val implicitRules: String? = null,
  val instantiatesCanonical: List<String> = listOf(),
  val instantiatesUri: List<String> = listOf(),
  /**
   * proposal | plan | directive | order | original-order | reflex-order | filler-order |
   * instance-order | option
   */
  val intent: String? = null,
  /**
   * Language of the resource content
   */
  override val language: String? = null,
  /**
   * Metadata about the resource
   */
  override val meta: Meta? = null,
  override val modifierExtension: List<Extension> = listOf(),
  val note: List<Annotation> = listOf(),
  /**
   * routine | urgent | asap | stat
   */
  val priority: String? = null,
  val reasonCode: List<CodeableConcept> = listOf(),
  val reasonReference: List<Reference> = listOf(),
  val replaces: List<Reference> = listOf(),
  /**
   * draft | active | on-hold | revoked | completed | entered-in-error | unknown
   */
  val status: String? = null,
  /**
   * Who the request group is about
   */
  val subject: Reference? = null,
  /**
   * Text summary of the resource, for human interpretation
   */
  override val text: Narrative? = null
) : DomainResource
