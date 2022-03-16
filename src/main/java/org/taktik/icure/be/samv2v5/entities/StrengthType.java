/*
 * Copyright (c) 2020. Taktik SA, All rights reserved.
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2020.10.15 at 03:32:18 PM CEST
//


package org.taktik.icure.be.samv2v5.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for StrengthType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="StrengthType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Numerator" type="{urn:be:fgov:ehealth:samws:v2:core}QuantityType"/>
 *         &lt;element name="Denominator" type="{urn:be:fgov:ehealth:samws:v2:core}QuantityType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StrengthType", namespace = "urn:be:fgov:ehealth:samws:v2:core", propOrder = {
    "numerator",
    "denominator"
})
public class StrengthType {

    @XmlElement(name = "Numerator", namespace = "urn:be:fgov:ehealth:samws:v2:core", required = true)
    protected QuantityType numerator;
    @XmlElement(name = "Denominator", namespace = "urn:be:fgov:ehealth:samws:v2:core", required = true)
    protected QuantityType denominator;

    /**
     * Gets the value of the numerator property.
     *
     * @return
     *     possible object is
     *     {@link QuantityType }
     *
     */
    public QuantityType getNumerator() {
        return numerator;
    }

    /**
     * Sets the value of the numerator property.
     *
     * @param value
     *     allowed object is
     *     {@link QuantityType }
     *
     */
    public void setNumerator(QuantityType value) {
        this.numerator = value;
    }

    /**
     * Gets the value of the denominator property.
     *
     * @return
     *     possible object is
     *     {@link QuantityType }
     *
     */
    public QuantityType getDenominator() {
        return denominator;
    }

    /**
     * Sets the value of the denominator property.
     *
     * @param value
     *     allowed object is
     *     {@link QuantityType }
     *
     */
    public void setDenominator(QuantityType value) {
        this.denominator = value;
    }

}
