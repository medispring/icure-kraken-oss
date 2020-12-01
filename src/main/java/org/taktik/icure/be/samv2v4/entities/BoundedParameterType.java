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
 * <p>Java class for BoundedParameterType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="BoundedParameterType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DosageParameterName" type="{urn:be:fgov:ehealth:samws:v2:core}String255Type"/>
 *         &lt;element name="LowerBound" type="{urn:be:fgov:ehealth:samws:v2:core}QuantityType" minOccurs="0"/>
 *         &lt;element name="UpperBound" type="{urn:be:fgov:ehealth:samws:v2:core}QuantityType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BoundedParameterType", namespace = "urn:be:fgov:ehealth:samws:v2:virtual:common", propOrder = {
    "dosageParameterName",
    "lowerBound",
    "upperBound"
})
public class BoundedParameterType {

    @XmlElement(name = "DosageParameterName", required = true)
    protected String dosageParameterName;
    @XmlElement(name = "LowerBound")
    protected QuantityType lowerBound;
    @XmlElement(name = "UpperBound")
    protected QuantityType upperBound;

    /**
     * Gets the value of the dosageParameterName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDosageParameterName() {
        return dosageParameterName;
    }

    /**
     * Sets the value of the dosageParameterName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDosageParameterName(String value) {
        this.dosageParameterName = value;
    }

    /**
     * Gets the value of the lowerBound property.
     *
     * @return
     *     possible object is
     *     {@link QuantityType }
     *
     */
    public QuantityType getLowerBound() {
        return lowerBound;
    }

    /**
     * Sets the value of the lowerBound property.
     *
     * @param value
     *     allowed object is
     *     {@link QuantityType }
     *
     */
    public void setLowerBound(QuantityType value) {
        this.lowerBound = value;
    }

    /**
     * Gets the value of the upperBound property.
     *
     * @return
     *     possible object is
     *     {@link QuantityType }
     *
     */
    public QuantityType getUpperBound() {
        return upperBound;
    }

    /**
     * Sets the value of the upperBound property.
     *
     * @param value
     *     allowed object is
     *     {@link QuantityType }
     *
     */
    public void setUpperBound(QuantityType value) {
        this.upperBound = value;
    }

}
