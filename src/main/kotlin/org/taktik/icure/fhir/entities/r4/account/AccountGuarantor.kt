//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.fhir.entities.r4.account

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.fhir.entities.r4.backboneelement.BackboneElement
import org.taktik.icure.fhir.entities.r4.extension.Extension
import org.taktik.icure.fhir.entities.r4.period.Period
import org.taktik.icure.fhir.entities.r4.reference.Reference

/**
 * The parties ultimately responsible for balancing the Account
 *
 * The parties responsible for balancing the account if other payment options fall short.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class AccountGuarantor(
  override val extension: List<Extension> = listOf(),
  /**
   * Unique id for inter-element referencing
   */
  override val id: String? = null,
  override val modifierExtension: List<Extension> = listOf(),
  /**
   * Credit or other hold applied
   */
  val onHold: Boolean? = null,
  /**
   * Responsible entity
   */
  val party: Reference,
  /**
   * Guarantee account during
   */
  val period: Period? = null
) : BackboneElement
