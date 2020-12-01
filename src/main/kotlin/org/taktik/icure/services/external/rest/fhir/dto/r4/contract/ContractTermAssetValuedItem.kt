//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.services.external.rest.fhir.dto.r4.contract

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.services.external.rest.fhir.dto.r4.Quantity
import org.taktik.icure.services.external.rest.fhir.dto.r4.backboneelement.BackboneElement
import org.taktik.icure.services.external.rest.fhir.dto.r4.codeableconcept.CodeableConcept
import org.taktik.icure.services.external.rest.fhir.dto.r4.extension.Extension
import org.taktik.icure.services.external.rest.fhir.dto.r4.identifier.Identifier
import org.taktik.icure.services.external.rest.fhir.dto.r4.money.Money
import org.taktik.icure.services.external.rest.fhir.dto.r4.reference.Reference

/**
 * Contract Valued Item List
 *
 * Contract Valued Item List.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class ContractTermAssetValuedItem(
  /**
   * Contract Valued Item Effective Tiem
   */
  val effectiveTime: String? = null,
  /**
   * Contract Valued Item Type
   */
  val entityCodeableConcept: CodeableConcept? = null,
  /**
   * Contract Valued Item Type
   */
  val entityReference: Reference? = null,
  override val extension: List<Extension> = listOf(),
  /**
   * Contract Valued Item Price Scaling Factor
   */
  val factor: Float? = null,
  /**
   * Unique id for inter-element referencing
   */
  override val id: String? = null,
  /**
   * Contract Valued Item Number
   */
  val identifier: Identifier? = null,
  val linkId: List<String> = listOf(),
  override val modifierExtension: List<Extension> = listOf(),
  /**
   * Total Contract Valued Item Value
   */
  val net: Money? = null,
  /**
   * Terms of valuation
   */
  val payment: String? = null,
  /**
   * When payment is due
   */
  val paymentDate: String? = null,
  /**
   * Contract Valued Item Difficulty Scaling Factor
   */
  val points: Float? = null,
  /**
   * Count of Contract Valued Items
   */
  val quantity: Quantity? = null,
  /**
   * Who will receive payment
   */
  val recipient: Reference? = null,
  /**
   * Who will make payment
   */
  val responsible: Reference? = null,
  val securityLabelNumber: List<Int> = listOf(),
  /**
   * Contract Valued Item fee, charge, or cost
   */
  val unitPrice: Money? = null
) : BackboneElement
