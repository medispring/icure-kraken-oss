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

package org.taktik.commons.filters.queryfilterproviders;

public class DoubleRange {
	private Double fromValue;
	private Double toValue;

	public DoubleRange(Double fromValue, Double toValue) {
		this.fromValue = fromValue;
		this.toValue = toValue;
	}

	public Double getFromValue() {
		return fromValue;
	}

	public void setFromValue(Double fromValue) {
		this.fromValue = fromValue;
	}

	public Double getToValue() {
		return toValue;
	}

	public void setToValue(Double toValue) {
		this.toValue = toValue;
	}

	public boolean isNull() {
		return fromValue == null && toValue == null;
	}
}
