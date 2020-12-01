/*
 *  iCure Data Stack. Copyright (c) 2020 Taktik SA
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU Affero General Public License as
 *     published by the Free Software Foundation, either version 3 of the
 *     License, or (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful, but
 *     WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *     Affero General Public License for more details.
 *
 *     You should have received a copy of the GNU Affero General Public
 *     License along with this program.  If not, see
 *     <https://www.gnu.org/licenses/>.
 */

//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.fhir.entities.r4.organizationaffiliation

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.fhir.entities.r4.DomainResource
import org.taktik.icure.fhir.entities.r4.Meta
import org.taktik.icure.fhir.entities.r4.Resource
import org.taktik.icure.fhir.entities.r4.codeableconcept.CodeableConcept
import org.taktik.icure.fhir.entities.r4.contactpoint.ContactPoint
import org.taktik.icure.fhir.entities.r4.extension.Extension
import org.taktik.icure.fhir.entities.r4.identifier.Identifier
import org.taktik.icure.fhir.entities.r4.narrative.Narrative
import org.taktik.icure.fhir.entities.r4.period.Period
import org.taktik.icure.fhir.entities.r4.reference.Reference

/**
 * Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a
 * part-of relationship/sub-division relationship
 *
 * Defines an affiliation/assotiation/relationship between 2 distinct oganizations, that is not a
 * part-of relationship/sub-division relationship.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class OrganizationAffiliation(
  /**
   * Whether this organization affiliation record is in active use
   */
  val active: Boolean? = null,
  val code: List<CodeableConcept> = listOf(),
  override val contained: List<Resource> = listOf(),
  val endpoint: List<Reference> = listOf(),
  override val extension: List<Extension> = listOf(),
  val healthcareService: List<Reference> = listOf(),
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
  val location: List<Reference> = listOf(),
  /**
   * Metadata about the resource
   */
  override val meta: Meta? = null,
  override val modifierExtension: List<Extension> = listOf(),
  val network: List<Reference> = listOf(),
  /**
   * Organization where the role is available
   */
  val organization: Reference? = null,
  /**
   * Organization that provides/performs the role (e.g. providing services or is a member of)
   */
  val participatingOrganization: Reference? = null,
  /**
   * The period during which the participatingOrganization is affiliated with the primary
   * organization
   */
  val period: Period? = null,
  val specialty: List<CodeableConcept> = listOf(),
  val telecom: List<ContactPoint> = listOf(),
  /**
   * Text summary of the resource, for human interpretation
   */
  override val text: Narrative? = null
) : DomainResource
