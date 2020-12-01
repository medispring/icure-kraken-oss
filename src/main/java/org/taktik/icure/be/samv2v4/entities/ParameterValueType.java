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
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2020.05.12 at 04:36:37 PM CEST
//


package org.taktik.icure.be.samv2v4.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ParameterValueType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ParameterValueType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;choice>
 *         &lt;element name="Quantity" type="{urn:be:fgov:ehealth:samws:v2:core}QuantityType"/>
 *         &lt;element name="QuantityRange" type="{urn:be:fgov:ehealth:samws:v2:core}RangeType"/>
 *         &lt;element name="Strength" type="{urn:be:fgov:ehealth:samws:v2:core}StrengthType"/>
 *         &lt;element name="StrengthRange" type="{urn:be:fgov:ehealth:samws:v2:core}StrengthRangeType"/>
 *         &lt;element name="Code" type="{urn:be:fgov:ehealth:samws:v2:core}String20Type"/>
 *         &lt;element name="Boolean" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/choice>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ParameterValueType", namespace = "urn:be:fgov:ehealth:samws:v2:reimbursementlaw:submit", propOrder = {
    "quantity",
    "quantityRange",
    "strength",
    "strengthRange",
    "code",
    "_boolean"
})
public class ParameterValueType {

    @XmlElement(name = "Quantity")
    protected QuantityType quantity;
    @XmlElement(name = "QuantityRange")
    protected RangeType quantityRange;
    @XmlElement(name = "Strength")
    protected StrengthType strength;
    @XmlElement(name = "StrengthRange")
    protected StrengthRangeType strengthRange;
    @XmlElement(name = "Code")
    protected String code;
    @XmlElement(name = "Boolean")
    protected Boolean _boolean;

    /**
     * Gets the value of the quantity property.
     *
     * @return
     *     possible object is
     *     {@link QuantityType }
     *
     */
    public QuantityType getQuantity() {
        return quantity;
    }

    /**
     * Sets the value of the quantity property.
     *
     * @param value
     *     allowed object is
     *     {@link QuantityType }
     *
     */
    public void setQuantity(QuantityType value) {
        this.quantity = value;
    }

    /**
     * Gets the value of the quantityRange property.
     *
     * @return
     *     possible object is
     *     {@link RangeType }
     *
     */
    public RangeType getQuantityRange() {
        return quantityRange;
    }

    /**
     * Sets the value of the quantityRange property.
     *
     * @param value
     *     allowed object is
     *     {@link RangeType }
     *
     */
    public void setQuantityRange(RangeType value) {
        this.quantityRange = value;
    }

    /**
     * Gets the value of the strength property.
     *
     * @return
     *     possible object is
     *     {@link StrengthType }
     *
     */
    public StrengthType getStrength() {
        return strength;
    }

    /**
     * Sets the value of the strength property.
     *
     * @param value
     *     allowed object is
     *     {@link StrengthType }
     *
     */
    public void setStrength(StrengthType value) {
        this.strength = value;
    }

    /**
     * Gets the value of the strengthRange property.
     *
     * @return
     *     possible object is
     *     {@link StrengthRangeType }
     *
     */
    public StrengthRangeType getStrengthRange() {
        return strengthRange;
    }

    /**
     * Sets the value of the strengthRange property.
     *
     * @param value
     *     allowed object is
     *     {@link StrengthRangeType }
     *
     */
    public void setStrengthRange(StrengthRangeType value) {
        this.strengthRange = value;
    }

    /**
     * Gets the value of the code property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the boolean property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isBoolean() {
        return _boolean;
    }

    /**
     * Sets the value of the boolean property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setBoolean(Boolean value) {
        this._boolean = value;
    }

}
