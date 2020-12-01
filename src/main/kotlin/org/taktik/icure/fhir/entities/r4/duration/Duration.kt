//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.fhir.entities.r4.duration

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.fhir.entities.r4.Quantity
import org.taktik.icure.fhir.entities.r4.extension.Extension

/**
 * A length of time
 *
 * A length of time.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class Duration(
  /**
   * Coded form of the unit
   */
  override val code: String? = null,
  /**
   * < | <= | >= | > - how to understand the value
   */
  override val comparator: String? = null,
  override val extension: List<Extension> = listOf(),
  /**
   * Unique id for inter-element referencing
   */
  override val id: String? = null,
  /**
   * System that defines coded unit form
   */
  override val system: String? = null,
  /**
   * Unit representation
   */
  override val unit: String? = null,
  /**
   * Numerical value (with implicit precision)
   */
  override val value: Float? = null
) : Quantity
