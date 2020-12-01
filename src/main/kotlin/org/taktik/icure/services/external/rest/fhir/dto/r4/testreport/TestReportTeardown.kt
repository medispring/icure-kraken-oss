//
//  Generated from FHIR Version 4.0.1-9346c8cc45
//
package org.taktik.icure.services.external.rest.fhir.dto.r4.testreport

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.github.pozo.KotlinBuilder
import org.taktik.icure.services.external.rest.fhir.dto.r4.backboneelement.BackboneElement
import org.taktik.icure.services.external.rest.fhir.dto.r4.extension.Extension

/**
 * The results of running the series of required clean up steps
 *
 * The results of the series of operations required to clean up after all the tests were executed
 * (successfully or otherwise).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@KotlinBuilder
data class TestReportTeardown(
  val action: List<TestReportTeardownAction> = listOf(),
  override val extension: List<Extension> = listOf(),
  /**
   * Unique id for inter-element referencing
   */
  override val id: String? = null,
  override val modifierExtension: List<Extension> = listOf()
) : BackboneElement
