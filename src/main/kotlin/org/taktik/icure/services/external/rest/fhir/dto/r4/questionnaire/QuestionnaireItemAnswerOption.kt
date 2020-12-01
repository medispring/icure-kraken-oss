//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.services.external.rest.fhir.dto.r4.questionnaire

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.services.external.rest.fhir.dto.r4.backboneelement.BackboneElement
import org.taktik.icure.services.external.rest.fhir.dto.r4.coding.Coding
import org.taktik.icure.services.external.rest.fhir.dto.r4.extension.Extension
import org.taktik.icure.services.external.rest.fhir.dto.r4.reference.Reference

/**
 * Permitted answer
 *
 * One of the permitted answers for a "choice" or "open-choice" question.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class QuestionnaireItemAnswerOption(
  override val extension: List<Extension> = listOf(),
  /**
   * Unique id for inter-element referencing
   */
  override val id: String? = null,
  /**
   * Whether option is selected by default
   */
  val initialSelected: Boolean? = null,
  override val modifierExtension: List<Extension> = listOf(),
  /**
   * Answer value
   */
  val valueCoding: Coding,
  /**
   * Answer value
   */
  val valueDate: String? = null,
  /**
   * Answer value
   */
  val valueInteger: Int? = null,
  /**
   * Answer value
   */
  val valueReference: Reference,
  /**
   * Answer value
   */
  val valueString: String? = null,
  /**
   * Answer value
   */
  val valueTime: String? = null
) : BackboneElement
