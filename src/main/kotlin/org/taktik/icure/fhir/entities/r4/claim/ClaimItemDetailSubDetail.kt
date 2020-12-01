//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.fhir.entities.r4.claim

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.fhir.entities.r4.Quantity
import org.taktik.icure.fhir.entities.r4.backboneelement.BackboneElement
import org.taktik.icure.fhir.entities.r4.codeableconcept.CodeableConcept
import org.taktik.icure.fhir.entities.r4.extension.Extension
import org.taktik.icure.fhir.entities.r4.money.Money
import org.taktik.icure.fhir.entities.r4.reference.Reference

/**
 * Product or service provided
 *
 * A claim detail line. Either a simple (a product or service) or a 'group' of sub-details which are
 * simple items.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class ClaimItemDetailSubDetail(
  /**
   * Benefit classification
   */
  val category: CodeableConcept? = null,
  override val extension: List<Extension> = listOf(),
  /**
   * Price scaling factor
   */
  val factor: Float? = null,
  /**
   * Unique id for inter-element referencing
   */
  override val id: String? = null,
  val modifier: List<CodeableConcept> = listOf(),
  override val modifierExtension: List<Extension> = listOf(),
  /**
   * Total item cost
   */
  val net: Money? = null,
  /**
   * Billing, service, product, or drug code
   */
  val productOrService: CodeableConcept,
  val programCode: List<CodeableConcept> = listOf(),
  /**
   * Count of products or services
   */
  val quantity: Quantity? = null,
  /**
   * Revenue or cost center code
   */
  val revenue: CodeableConcept? = null,
  /**
   * Item instance identifier
   */
  val sequence: Int? = null,
  val udi: List<Reference> = listOf(),
  /**
   * Fee, charge or cost per item
   */
  val unitPrice: Money? = null
) : BackboneElement
