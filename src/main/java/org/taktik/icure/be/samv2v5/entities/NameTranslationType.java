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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for NameTranslationType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="NameTranslationType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:chapteriv:submit}NameTranslationKeyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:chapteriv:submit}NameTranslationFields"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NameTranslationType", namespace = "urn:be:fgov:ehealth:samws:v2:chapteriv:submit", propOrder = {
    "createdTimestamp",
    "createdUserId",
    "shortText",
    "longText",
    "longBinaryText",
    "addressUrl",
    "modificationStatus"
})
public class NameTranslationType
    extends NameTranslationKeyType
{

    @XmlElement(name = "CreatedTimestamp", namespace = "urn:be:fgov:ehealth:samws:v2:chapteriv:submit", required = true)
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdTimestamp;
    @XmlElement(name = "CreatedUserId", namespace = "urn:be:fgov:ehealth:samws:v2:chapteriv:submit", required = true)
    protected String createdUserId;
    @XmlElement(name = "ShortText", namespace = "urn:be:fgov:ehealth:samws:v2:chapteriv:submit")
    protected String shortText;
    @XmlElement(name = "LongText", namespace = "urn:be:fgov:ehealth:samws:v2:chapteriv:submit")
    protected String longText;
    @XmlElement(name = "LongBinaryText", namespace = "urn:be:fgov:ehealth:samws:v2:chapteriv:submit")
    protected byte[] longBinaryText;
    @XmlElement(name = "AddressUrl", namespace = "urn:be:fgov:ehealth:samws:v2:chapteriv:submit")
    protected String addressUrl;
    @XmlElement(name = "ModificationStatus", namespace = "urn:be:fgov:ehealth:samws:v2:chapteriv:submit", required = true)
    protected String modificationStatus;

    /**
     * Gets the value of the createdTimestamp property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getCreatedTimestamp() {
        return createdTimestamp;
    }

    /**
     * Sets the value of the createdTimestamp property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setCreatedTimestamp(XMLGregorianCalendar value) {
        this.createdTimestamp = value;
    }

    /**
     * Gets the value of the createdUserId property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCreatedUserId() {
        return createdUserId;
    }

    /**
     * Sets the value of the createdUserId property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCreatedUserId(String value) {
        this.createdUserId = value;
    }

    /**
     * Gets the value of the shortText property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getShortText() {
        return shortText;
    }

    /**
     * Sets the value of the shortText property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setShortText(String value) {
        this.shortText = value;
    }

    /**
     * Gets the value of the longText property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getLongText() {
        return longText;
    }

    /**
     * Sets the value of the longText property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setLongText(String value) {
        this.longText = value;
    }

    /**
     * Gets the value of the longBinaryText property.
     *
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getLongBinaryText() {
        return longBinaryText;
    }

    /**
     * Sets the value of the longBinaryText property.
     *
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setLongBinaryText(byte[] value) {
        this.longBinaryText = value;
    }

    /**
     * Gets the value of the addressUrl property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAddressUrl() {
        return addressUrl;
    }

    /**
     * Sets the value of the addressUrl property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAddressUrl(String value) {
        this.addressUrl = value;
    }

    /**
     * Gets the value of the modificationStatus property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getModificationStatus() {
        return modificationStatus;
    }

    /**
     * Sets the value of the modificationStatus property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setModificationStatus(String value) {
        this.modificationStatus = value;
    }

}
