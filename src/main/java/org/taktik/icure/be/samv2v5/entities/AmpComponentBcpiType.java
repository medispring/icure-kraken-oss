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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AmpComponentBcpiType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="AmpComponentBcpiType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:actual:common}AmpComponentKeyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}AmpComponentBcpiFields"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AmpComponentBcpiType", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common", propOrder = {
    "dividable",
    "scored",
    "crushable",
    "containsAlcohol",
    "sugarFree",
    "modifiedReleaseType",
    "specificDrugDevice",
    "dimensions",
    "name",
    "note",
    "concentration",
    "osmoticConcentration",
    "caloricValue"
})
@XmlSeeAlso({
    AddAmpComponentBcpiType.class
})
public class AmpComponentBcpiType
    extends AmpComponentKeyType
{

    @XmlElement(name = "Dividable")
    protected String dividable;
    @XmlElement(name = "Scored")
    protected String scored;
    @XmlElement(name = "Crushable")
    @XmlSchemaType(name = "string")
    protected CrushableType crushable;
    @XmlElement(name = "ContainsAlcohol")
    @XmlSchemaType(name = "string")
    protected ContainsAlcoholType containsAlcohol;
    @XmlElement(name = "SugarFree")
    protected Boolean sugarFree;
    @XmlElement(name = "ModifiedReleaseType")
    @XmlSchemaType(name = "integer")
    protected Integer modifiedReleaseType;
    @XmlElement(name = "SpecificDrugDevice")
    @XmlSchemaType(name = "integer")
    protected Integer specificDrugDevice;
    @XmlElement(name = "Dimensions")
    protected String dimensions;
    @XmlElement(name = "Name", required = true)
    protected Text255Type name;
    @XmlElement(name = "Note")
    protected TextType note;
    @XmlElement(name = "Concentration")
    protected Text255Type concentration;
    @XmlElement(name = "OsmoticConcentration")
    protected BigDecimal osmoticConcentration;
    @XmlElement(name = "CaloricValue")
    protected BigDecimal caloricValue;

    /**
     * Gets the value of the dividable property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDividable() {
        return dividable;
    }

    /**
     * Sets the value of the dividable property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDividable(String value) {
        this.dividable = value;
    }

    /**
     * Gets the value of the scored property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getScored() {
        return scored;
    }

    /**
     * Sets the value of the scored property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setScored(String value) {
        this.scored = value;
    }

    /**
     * Gets the value of the crushable property.
     *
     * @return
     *     possible object is
     *     {@link CrushableType }
     *
     */
    public CrushableType getCrushable() {
        return crushable;
    }

    /**
     * Sets the value of the crushable property.
     *
     * @param value
     *     allowed object is
     *     {@link CrushableType }
     *
     */
    public void setCrushable(CrushableType value) {
        this.crushable = value;
    }

    /**
     * Gets the value of the containsAlcohol property.
     *
     * @return
     *     possible object is
     *     {@link ContainsAlcoholType }
     *
     */
    public ContainsAlcoholType getContainsAlcohol() {
        return containsAlcohol;
    }

    /**
     * Sets the value of the containsAlcohol property.
     *
     * @param value
     *     allowed object is
     *     {@link ContainsAlcoholType }
     *
     */
    public void setContainsAlcohol(ContainsAlcoholType value) {
        this.containsAlcohol = value;
    }

    /**
     * Gets the value of the sugarFree property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isSugarFree() {
        return sugarFree;
    }

    /**
     * Sets the value of the sugarFree property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSugarFree(Boolean value) {
        this.sugarFree = value;
    }

    /**
     * Gets the value of the modifiedReleaseType property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getModifiedReleaseType() {
        return modifiedReleaseType;
    }

    /**
     * Sets the value of the modifiedReleaseType property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setModifiedReleaseType(Integer value) {
        this.modifiedReleaseType = value;
    }

    /**
     * Gets the value of the specificDrugDevice property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getSpecificDrugDevice() {
        return specificDrugDevice;
    }

    /**
     * Sets the value of the specificDrugDevice property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setSpecificDrugDevice(Integer value) {
        this.specificDrugDevice = value;
    }

    /**
     * Gets the value of the dimensions property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getDimensions() {
        return dimensions;
    }

    /**
     * Sets the value of the dimensions property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setDimensions(String value) {
        this.dimensions = value;
    }

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
     * Gets the value of the note property.
     *
     * @return
     *     possible object is
     *     {@link TextType }
     *
     */
    public TextType getNote() {
        return note;
    }

    /**
     * Sets the value of the note property.
     *
     * @param value
     *     allowed object is
     *     {@link TextType }
     *
     */
    public void setNote(TextType value) {
        this.note = value;
    }

    /**
     * Gets the value of the concentration property.
     *
     * @return
     *     possible object is
     *     {@link Text255Type }
     *
     */
    public Text255Type getConcentration() {
        return concentration;
    }

    /**
     * Sets the value of the concentration property.
     *
     * @param value
     *     allowed object is
     *     {@link Text255Type }
     *
     */
    public void setConcentration(Text255Type value) {
        this.concentration = value;
    }

    /**
     * Gets the value of the osmoticConcentration property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getOsmoticConcentration() {
        return osmoticConcentration;
    }

    /**
     * Sets the value of the osmoticConcentration property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setOsmoticConcentration(BigDecimal value) {
        this.osmoticConcentration = value;
    }

    /**
     * Gets the value of the caloricValue property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getCaloricValue() {
        return caloricValue;
    }

    /**
     * Sets the value of the caloricValue property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setCaloricValue(BigDecimal value) {
        this.caloricValue = value;
    }

}
