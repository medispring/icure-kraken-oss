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
 * <p>Java class for PackagingTypeType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="PackagingTypeType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:refdata}PackagingTypeKeyType">
 *       &lt;sequence>
 *         &lt;element name="Name" type="{urn:be:fgov:ehealth:samws:v2:core}Text255Type"/>
 *         &lt;element name="EdqmCode" type="{urn:be:fgov:ehealth:samws:v2:core}String20Type" minOccurs="0"/>
 *         &lt;element name="EdqmDefinition" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "PackagingTypeType", namespace = "urn:be:fgov:ehealth:samws:v2:refdata", propOrder = {
    "name",
    "edqmCode",
    "edqmDefinition"
})
public class PackagingTypeType
    extends PackagingTypeKeyType
{

    @XmlElement(name = "Name", namespace = "urn:be:fgov:ehealth:samws:v2:refdata", required = true)
    protected Text255Type name;
    @XmlElement(name = "EdqmCode", namespace = "urn:be:fgov:ehealth:samws:v2:refdata")
    protected String edqmCode;
    @XmlElement(name = "EdqmDefinition", namespace = "urn:be:fgov:ehealth:samws:v2:refdata")
    protected String edqmDefinition;

    /**
     * Gets the value of the name property.
     *
     * @return
     *     possible object is
     *     {@link Text255Type }
     *
     */
    public Text255Type getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value
     *     allowed object is
     *     {@link Text255Type }
     *
     */
    public void setName(Text255Type value) {
        this.name = value;
    }

    /**
     * Gets the value of the edqmCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEdqmCode() {
        return edqmCode;
    }

    /**
     * Sets the value of the edqmCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEdqmCode(String value) {
        this.edqmCode = value;
    }

    /**
     * Gets the value of the edqmDefinition property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getEdqmDefinition() {
        return edqmDefinition;
    }

    /**
     * Sets the value of the edqmDefinition property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setEdqmDefinition(String value) {
        this.edqmDefinition = value;
    }

}
