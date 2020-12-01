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
package org.taktik.icure.fhir.entities.r4

/**
 * Base Resource
 *
 * This is the base resource type for everything.
 */
interface Resource {
  /**
   * Logical id of this artifact
   */
  val id: String?

  /**
   * A set of rules under which this content was created
   */
  val implicitRules: String?

  /**
   * Language of the resource content
   */
  val language: String?

  /**
   * Metadata about the resource
   */
  val meta: Meta?
}
