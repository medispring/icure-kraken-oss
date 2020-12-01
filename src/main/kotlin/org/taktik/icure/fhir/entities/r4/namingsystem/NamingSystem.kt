//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.fhir.entities.r4.namingsystem

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.fhir.entities.r4.DomainResource
import org.taktik.icure.fhir.entities.r4.Meta
import org.taktik.icure.fhir.entities.r4.Resource
import org.taktik.icure.fhir.entities.r4.codeableconcept.CodeableConcept
import org.taktik.icure.fhir.entities.r4.contactdetail.ContactDetail
import org.taktik.icure.fhir.entities.r4.extension.Extension
import org.taktik.icure.fhir.entities.r4.narrative.Narrative
import org.taktik.icure.fhir.entities.r4.usagecontext.UsageContext

/**
 * System of unique identification
 *
 * A curated namespace that issues unique symbols within that namespace for the identification of
 * concepts, people, devices, etc.  Represents a "System" used within the Identifier and Coding data
 * types.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class NamingSystem(
  val contact: List<ContactDetail> = listOf(),
  override val contained: List<Resource> = listOf(),
  /**
   * Date last changed
   */
  val date: String? = null,
  /**
   * Natural language description of the naming system
   */
  val description: String? = null,
  override val extension: List<Extension> = listOf(),
  /**
   * Logical id of this artifact
   */
  override val id: String? = null,
  /**
   * A set of rules under which this content was created
   */
  override val implicitRules: String? = null,
  val jurisdiction: List<CodeableConcept> = listOf(),
  /**
   * codesystem | identifier | root
   */
  val kind: String? = null,
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
   * Name for this naming system (computer friendly)
   */
  val name: String? = null,
  /**
   * Name of the publisher (organization or individual)
   */
  val publisher: String? = null,
  /**
   * Who maintains system namespace?
   */
  val responsible: String? = null,
  /**
   * draft | active | retired | unknown
   */
  val status: String? = null,
  /**
   * Text summary of the resource, for human interpretation
   */
  override val text: Narrative? = null,
  /**
   * e.g. driver,  provider,  patient, bank etc.
   */
  val type: CodeableConcept? = null,
  val uniqueId: List<NamingSystemUniqueId> = listOf(),
  /**
   * How/where is it used
   */
  val usage: String? = null,
  val useContext: List<UsageContext> = listOf()
) : DomainResource
