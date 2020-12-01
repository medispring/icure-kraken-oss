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
// Generated on: 2019.05.22 at 08:11:32 PM CEST
//


package org.taktik.icure.be.samv2.entities;

import org.taktik.icure.be.ehealth.samws.v2.actual.common.AmpStatusType;
import org.taktik.icure.be.ehealth.samws.v2.actual.common.MedicineTypeType;
import org.taktik.icure.be.ehealth.samws.v2.core.Text255Type;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * <p>Java class for AmpDataType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="AmpDataType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:export}DataPeriodType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}AmpFamhpFields"/>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:export}AmpFamhpReferenceFields"/>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}AmpBcpiFields" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AmpDataType", propOrder = {
        "officialName",
        "status",
        "name",
        "blackTriangle",
        "medicineType",
        "company",
        "abbreviatedName",
        "proprietarySuffix",
        "prescriptionName"
})
public class AmpDataType
        extends DataPeriodType
        implements Serializable {

    private final static long serialVersionUID = 2L;
    @XmlElement(name = "OfficialName", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common", required = true)
    protected String officialName;
    @XmlElement(name = "Status", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    @XmlSchemaType(name = "string")
    protected AmpStatusType status;
    @XmlElement(name = "Name", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common", required = true)
    protected Text255Type name;
    @XmlElement(name = "BlackTriangle", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected boolean blackTriangle;
    @XmlElement(name = "MedicineType", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common", required = true)
    @XmlSchemaType(name = "string")
    protected MedicineTypeType medicineType;
    @XmlElement(name = "Company", required = true)
    protected CompanyFullDataType company;
    @XmlElement(name = "AbbreviatedName", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Text255Type abbreviatedName;
    @XmlElement(name = "ProprietarySuffix", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Text255Type proprietarySuffix;
    @XmlElement(name = "PrescriptionName", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Text255Type prescriptionName;

    /**
     * Gets the value of the officialName property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOfficialName() {
        return officialName;
    }

    /**
     * Sets the value of the officialName property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOfficialName(String value) {
        this.officialName = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return possible object is
     * {@link AmpStatusType }
     */
    public AmpStatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value allowed object is
     *              {@link AmpStatusType }
     */
    public void setStatus(AmpStatusType value) {
        this.status = value;
    }

    /**
     * Gets the value of the name property.
     *
     * @return possible object is
     * {@link Text255Type }
     */
    public Text255Type getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     *
     * @param value allowed object is
     *              {@link Text255Type }
     */
    public void setName(Text255Type value) {
        this.name = value;
    }

    /**
     * Gets the value of the blackTriangle property.
     */
    public boolean isBlackTriangle() {
        return blackTriangle;
    }

    /**
     * Sets the value of the blackTriangle property.
     */
    public void setBlackTriangle(boolean value) {
        this.blackTriangle = value;
    }

    /**
     * Gets the value of the medicineType property.
     *
     * @return possible object is
     * {@link MedicineTypeType }
     */
    public MedicineTypeType getMedicineType() {
        return medicineType;
    }

    /**
     * Sets the value of the medicineType property.
     *
     * @param value allowed object is
     *              {@link MedicineTypeType }
     */
    public void setMedicineType(MedicineTypeType value) {
        this.medicineType = value;
    }

    /**
     * Gets the value of the company property.
     *
     * @return possible object is
     * {@link CompanyFullDataType }
     */
    public CompanyFullDataType getCompany() {
        return company;
    }

    /**
     * Sets the value of the company property.
     *
     * @param value allowed object is
     *              {@link CompanyFullDataType }
     */
    public void setCompany(CompanyFullDataType value) {
        this.company = value;
    }

    /**
     * Gets the value of the abbreviatedName property.
     *
     * @return possible object is
     * {@link Text255Type }
     */
    public Text255Type getAbbreviatedName() {
        return abbreviatedName;
    }

    /**
     * Sets the value of the abbreviatedName property.
     *
     * @param value allowed object is
     *              {@link Text255Type }
     */
    public void setAbbreviatedName(Text255Type value) {
        this.abbreviatedName = value;
    }

    /**
     * Gets the value of the proprietarySuffix property.
     *
     * @return possible object is
     * {@link Text255Type }
     */
    public Text255Type getProprietarySuffix() {
        return proprietarySuffix;
    }

    /**
     * Sets the value of the proprietarySuffix property.
     *
     * @param value allowed object is
     *              {@link Text255Type }
     */
    public void setProprietarySuffix(Text255Type value) {
        this.proprietarySuffix = value;
    }

    /**
     * Gets the value of the prescriptionName property.
     *
     * @return possible object is
     * {@link Text255Type }
     */
    public Text255Type getPrescriptionName() {
        return prescriptionName;
    }

    /**
     * Sets the value of the prescriptionName property.
     *
     * @param value allowed object is
     *              {@link Text255Type }
     */
    public void setPrescriptionName(Text255Type value) {
        this.prescriptionName = value;
    }

}
