//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.fhir.entities.r4.practitioner

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.github.pozo.KotlinBuilder
import org.ektorp.Attachment
import org.taktik.icure.entities.base.StoredDocument
import org.taktik.icure.entities.embed.RevisionInfo
import org.taktik.icure.fhir.entities.r4.DomainResource
import org.taktik.icure.fhir.entities.r4.Meta
import org.taktik.icure.fhir.entities.r4.Resource
import org.taktik.icure.fhir.entities.r4.address.Address
import org.taktik.icure.fhir.entities.r4.codeableconcept.CodeableConcept
import org.taktik.icure.fhir.entities.r4.contactpoint.ContactPoint
import org.taktik.icure.fhir.entities.r4.extension.Extension
import org.taktik.icure.fhir.entities.r4.humanname.HumanName
import org.taktik.icure.fhir.entities.r4.identifier.Identifier
import org.taktik.icure.fhir.entities.r4.narrative.Narrative

/**
 * A person with a  formal responsibility in the provisioning of healthcare or related services
 *
 * A person who is directly or indirectly involved in the provisioning of healthcare.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class Practitioner(
        @JsonProperty("_rev")
  override val rev: String? = null,
        @JsonProperty("deleted")
  override val deletionDate: Long? = null,
        @JsonProperty("_attachments")
  override val attachments: Map<String, Attachment>? = null,
        @JsonProperty("_revs_info")
  override val revisionsInfo: List<RevisionInfo>? = null,
        @JsonProperty("_conflicts")
  override val conflicts: List<String>? = null,
        @JsonProperty("rev_history")
  override val revHistory: Map<String, String>? = null,
        /**
   * Whether this practitioner's record is in active use
   */
  val active: Boolean? = null,
        val address: List<Address> = listOf(),
        /**
   * The date  on which the practitioner was born
   */
  val birthDate: String? = null,
        val communication: List<CodeableConcept> = listOf(),
        override val contained: List<Resource> = listOf(),
        override val extension: List<Extension> = listOf(),
        /**
   * male | female | other | unknown
   */
  val gender: String? = null,
        /**
   * Logical id of this artifact
   */
  @JsonProperty("_id")
  override val id: String,
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
        val name: List<HumanName> = listOf(),
        val photo: List<org.taktik.icure.fhir.entities.r4.attachment.Attachment> = listOf(),
        val qualification: List<PractitionerQualification> = listOf(),
        val telecom: List<ContactPoint> = listOf(),
        /**
   * Text summary of the resource, for human interpretation
   */
  override val text: Narrative? = null
) : StoredDocument, DomainResource {
  override fun withIdRev(id: String?, rev: String): Practitioner = if (id != null) this.copy(id =
      id, rev = rev) else this.copy(rev = rev)

  override fun withDeletionDate(deletionDate: Long?): Practitioner = this.copy(deletionDate =
      deletionDate)
}
