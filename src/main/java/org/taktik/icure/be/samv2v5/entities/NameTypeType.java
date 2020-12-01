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
// Generated on: 2020.10.15 at 03:32:18 PM CEST
//


package org.taktik.icure.be.samv2v5.entities;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for NameTypeType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="NameTypeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="NameId" type="{urn:be:fgov:ehealth:samws:v2:core}Number10Type"/>
 *         &lt;element name="NameType" type="{urn:be:fgov:ehealth:samws:v2:core}String50Type" minOccurs="0"/>
 *         &lt;element name="NameTypeSeq" type="{urn:be:fgov:ehealth:samws:v2:core}Number2Type" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="NameTypeCV" use="required" type="{urn:be:fgov:ehealth:samws:v2:core}String6Type" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NameTypeType", namespace = "urn:be:fgov:ehealth:samws:v2:refdata", propOrder = {
    "nameId",
    "nameType",
    "nameTypeSeq"
})
public class NameTypeType {

    @XmlElement(name = "NameId", required = true)
    protected BigDecimal nameId;
    @XmlElement(name = "NameType")
    protected String nameType;
    @XmlElement(name = "NameTypeSeq")
    protected Integer nameTypeSeq;
    @XmlAttribute(name = "NameTypeCV", required = true)
    protected String nameTypeCV;

    /**
     * Gets the value of the nameId property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getNameId() {
        return nameId;
    }

    /**
     * Sets the value of the nameId property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setNameId(BigDecimal value) {
        this.nameId = value;
    }

    /**
     * Gets the value of the nameType property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNameType() {
        return nameType;
    }

    /**
     * Sets the value of the nameType property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNameType(String value) {
        this.nameType = value;
    }

    /**
     * Gets the value of the nameTypeSeq property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getNameTypeSeq() {
        return nameTypeSeq;
    }

    /**
     * Sets the value of the nameTypeSeq property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setNameTypeSeq(Integer value) {
        this.nameTypeSeq = value;
    }

    /**
     * Gets the value of the nameTypeCV property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getNameTypeCV() {
        return nameTypeCV;
    }

    /**
     * Sets the value of the nameTypeCV property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setNameTypeCV(String value) {
        this.nameTypeCV = value;
    }

}
