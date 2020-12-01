//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.services.external.rest.fhir.dto.r4.visionprescription

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.services.external.rest.fhir.dto.r4.Quantity
import org.taktik.icure.services.external.rest.fhir.dto.r4.annotation.Annotation
import org.taktik.icure.services.external.rest.fhir.dto.r4.backboneelement.BackboneElement
import org.taktik.icure.services.external.rest.fhir.dto.r4.codeableconcept.CodeableConcept
import org.taktik.icure.services.external.rest.fhir.dto.r4.extension.Extension

/**
 * Vision lens authorization
 *
 * Contain the details of  the individual lens specifications and serves as the authorization for
 * the fullfillment by certified professionals.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class VisionPrescriptionLensSpecification(
  /**
   * Added power for multifocal levels
   */
  val add: Float? = null,
  /**
   * Lens meridian which contain no power for astigmatism
   */
  val axis: Int? = null,
  /**
   * Contact lens back curvature
   */
  val backCurve: Float? = null,
  /**
   * Brand required
   */
  val brand: String? = null,
  /**
   * Color required
   */
  val color: String? = null,
  /**
   * Lens power for astigmatism
   */
  val cylinder: Float? = null,
  /**
   * Contact lens diameter
   */
  val diameter: Float? = null,
  /**
   * Lens wear duration
   */
  val duration: Quantity? = null,
  override val extension: List<Extension> = listOf(),
  /**
   * right | left
   */
  val eye: String? = null,
  /**
   * Unique id for inter-element referencing
   */
  override val id: String? = null,
  override val modifierExtension: List<Extension> = listOf(),
  val note: List<Annotation> = listOf(),
  /**
   * Contact lens power
   */
  val power: Float? = null,
  val prism: List<VisionPrescriptionLensSpecificationPrism> = listOf(),
  /**
   * Product to be supplied
   */
  val product: CodeableConcept,
  /**
   * Power of the lens
   */
  val sphere: Float? = null
) : BackboneElement
