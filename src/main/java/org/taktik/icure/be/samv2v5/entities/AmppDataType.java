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
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for AmppDataType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="AmppDataType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:export}DataPeriodType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}AmppFamhpFields"/>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}AmppFamhpFMDFields" minOccurs="0"/>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:export}AmppFamhpReferenceFields"/>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}AmppBcpiFields" minOccurs="0"/>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:export}AmppBcpiReferenceFields" minOccurs="0"/>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}AmppNihdiFields" minOccurs="0"/>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}AmppNihdiBisFields" minOccurs="0"/>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}AmppMinEcoFields" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AmppDataType", propOrder = {
    "authorisationNr",
    "orphan",
    "leafletLink",
    "spcLink",
    "rmaPatientLink",
    "rmaProfessionalLink",
    "parallelCircuit",
    "parallelDistributor",
    "packMultiplier",
    "packAmount",
    "packDisplayValue",
    "status",
    "gtin",
    "dhpcLink",
    "prescriptionNameFamhp",
    "fmdProductCode",
    "fmdInScope",
    "antiTemperingDevicePresent",
    "atc",
    "deliveryModus",
    "deliveryModusSpecification",
    "distributorCompany",
    "singleUse",
    "speciallyRegulated",
    "abbreviatedName",
    "prescriptionName",
    "note",
    "posologyNote",
    "crmLink",
    "noGenericPrescriptionReason",
    "exFactoryPrice",
    "reimbursementCode",
    "bigPackage",
    "index",
    "definedDailyDose",
    "officialExFactoryPrice",
    "realExFactoryPrice",
    "pricingInformationDecisionDate",
    "officialIndex"
})
public class AmppDataType
    extends DataPeriodType
{

    @XmlElement(name = "AuthorisationNr", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common", required = true)
    protected String authorisationNr;
    @XmlElement(name = "Orphan", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected boolean orphan;
    @XmlElement(name = "LeafletLink", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Text255Type leafletLink;
    @XmlElement(name = "SpcLink", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Text255Type spcLink;
    @XmlElement(name = "RmaPatientLink", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Text255Type rmaPatientLink;
    @XmlElement(name = "RmaProfessionalLink", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Text255Type rmaProfessionalLink;
    @XmlElement(name = "ParallelCircuit", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    @XmlSchemaType(name = "integer")
    protected Integer parallelCircuit;
    @XmlElement(name = "ParallelDistributor", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected String parallelDistributor;
    @XmlElement(name = "PackMultiplier", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Short packMultiplier;
    @XmlElement(name = "PackAmount", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected PackAmountType packAmount;
    @XmlElement(name = "PackDisplayValue", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected String packDisplayValue;
    @XmlElement(name = "Status", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common", required = true)
    @XmlSchemaType(name = "string")
    protected AmpStatusType status;
    @XmlElement(name = "GTIN", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected String gtin;
    @XmlElement(name = "DHPCLink", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Text255Type dhpcLink;
    @XmlElement(name = "PrescriptionNameFamhp", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Text255Type prescriptionNameFamhp;
    @XmlElement(name = "FMDProductCode", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected List<String> fmdProductCode;
    @XmlElement(name = "FMDInScope", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Boolean fmdInScope;
    @XmlElement(name = "AntiTemperingDevicePresent", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Boolean antiTemperingDevicePresent;
    @XmlElement(name = "Atc")
    protected List<AtcClassificationType> atc;
    @XmlElement(name = "DeliveryModus", required = true)
    protected DeliveryModusType deliveryModus;
    @XmlElement(name = "DeliveryModusSpecification")
    protected DeliveryModusSpecificationType deliveryModusSpecification;
    @XmlElement(name = "DistributorCompany")
    protected CompanyFullDataType distributorCompany;
    @XmlElement(name = "SingleUse", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Boolean singleUse;
    @XmlElement(name = "SpeciallyRegulated", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    @XmlSchemaType(name = "integer")
    protected Integer speciallyRegulated;
    @XmlElement(name = "AbbreviatedName", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Text255Type abbreviatedName;
    @XmlElement(name = "PrescriptionName", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Text255Type prescriptionName;
    @XmlElement(name = "Note", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected TextType note;
    @XmlElement(name = "PosologyNote", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected TextType posologyNote;
    @XmlElement(name = "CrmLink", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Text255Type crmLink;
    @XmlElement(name = "NoGenericPrescriptionReason")
    protected List<NoGenericPrescriptionReasonType> noGenericPrescriptionReason;
    @XmlElement(name = "ExFactoryPrice", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected BigDecimal exFactoryPrice;
    @XmlElement(name = "ReimbursementCode", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    @XmlSchemaType(name = "integer")
    protected Integer reimbursementCode;
    @XmlElement(name = "BigPackage", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Boolean bigPackage;
    @XmlElement(name = "Index", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected BigDecimal index;
    @XmlElement(name = "DefinedDailyDose", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected QuantityType definedDailyDose;
    @XmlElement(name = "OfficialExFactoryPrice", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected BigDecimal officialExFactoryPrice;
    @XmlElement(name = "RealExFactoryPrice", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected BigDecimal realExFactoryPrice;
    @XmlElement(name = "PricingInformationDecisionDate", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar pricingInformationDecisionDate;
    @XmlElement(name = "OfficialIndex", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected BigDecimal officialIndex;

    /**
     * Gets the value of the authorisationNr property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAuthorisationNr() {
        return authorisationNr;
    }

    /**
     * Sets the value of the authorisationNr property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAuthorisationNr(String value) {
        this.authorisationNr = value;
    }

    /**
     * Gets the value of the orphan property.
     *
     */
    public boolean isOrphan() {
        return orphan;
    }

    /**
     * Sets the value of the orphan property.
     *
     */
    public void setOrphan(boolean value) {
        this.orphan = value;
    }

    /**
     * Gets the value of the leafletLink property.
     *
     * @return
     *     possible object is
     *     {@link Text255Type }
     *
     */
    public Text255Type getLeafletLink() {
        return leafletLink;
    }

    /**
     * Sets the value of the leafletLink property.
     *
     * @param value
     *     allowed object is
     *     {@link Text255Type }
     *
     */
    public void setLeafletLink(Text255Type value) {
        this.leafletLink = value;
    }

    /**
     * Gets the value of the spcLink property.
     *
     * @return
     *     possible object is
     *     {@link Text255Type }
     *
     */
    public Text255Type getSpcLink() {
        return spcLink;
    }

    /**
     * Sets the value of the spcLink property.
     *
     * @param value
     *     allowed object is
     *     {@link Text255Type }
     *
     */
    public void setSpcLink(Text255Type value) {
        this.spcLink = value;
    }

    /**
     * Gets the value of the rmaPatientLink property.
     *
     * @return
     *     possible object is
     *     {@link Text255Type }
     *
     */
    public Text255Type getRmaPatientLink() {
        return rmaPatientLink;
    }

    /**
     * Sets the value of the rmaPatientLink property.
     *
     * @param value
     *     allowed object is
     *     {@link Text255Type }
     *
     */
    public void setRmaPatientLink(Text255Type value) {
        this.rmaPatientLink = value;
    }

    /**
     * Gets the value of the rmaProfessionalLink property.
     *
     * @return
     *     possible object is
     *     {@link Text255Type }
     *
     */
    public Text255Type getRmaProfessionalLink() {
        return rmaProfessionalLink;
    }

    /**
     * Sets the value of the rmaProfessionalLink property.
     *
     * @param value
     *     allowed object is
     *     {@link Text255Type }
     *
     */
    public void setRmaProfessionalLink(Text255Type value) {
        this.rmaProfessionalLink = value;
    }

    /**
     * Gets the value of the parallelCircuit property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getParallelCircuit() {
        return parallelCircuit;
    }

    /**
     * Sets the value of the parallelCircuit property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setParallelCircuit(Integer value) {
        this.parallelCircuit = value;
    }

    /**
     * Gets the value of the parallelDistributor property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getParallelDistributor() {
        return parallelDistributor;
    }

    /**
     * Sets the value of the parallelDistributor property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setParallelDistributor(String value) {
        this.parallelDistributor = value;
    }

    /**
     * Gets the value of the packMultiplier property.
     *
     * @return
     *     possible object is
     *     {@link Short }
     *
     */
    public Short getPackMultiplier() {
        return packMultiplier;
    }

    /**
     * Sets the value of the packMultiplier property.
     *
     * @param value
     *     allowed object is
     *     {@link Short }
     *
     */
    public void setPackMultiplier(Short value) {
        this.packMultiplier = value;
    }

    /**
     * Gets the value of the packAmount property.
     *
     * @return
     *     possible object is
     *     {@link PackAmountType }
     *
     */
    public PackAmountType getPackAmount() {
        return packAmount;
    }

    /**
     * Sets the value of the packAmount property.
     *
     * @param value
     *     allowed object is
     *     {@link PackAmountType }
     *
     */
    public void setPackAmount(PackAmountType value) {
        this.packAmount = value;
    }

    /**
     * Gets the value of the packDisplayValue property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getPackDisplayValue() {
        return packDisplayValue;
    }

    /**
     * Sets the value of the packDisplayValue property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setPackDisplayValue(String value) {
        this.packDisplayValue = value;
    }

    /**
     * Gets the value of the status property.
     *
     * @return
     *     possible object is
     *     {@link AmpStatusType }
     *
     */
    public AmpStatusType getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     *
     * @param value
     *     allowed object is
     *     {@link AmpStatusType }
     *
     */
    public void setStatus(AmpStatusType value) {
        this.status = value;
    }

    /**
     * Gets the value of the gtin property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getGTIN() {
        return gtin;
    }

    /**
     * Sets the value of the gtin property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setGTIN(String value) {
        this.gtin = value;
    }

    /**
     * Gets the value of the dhpcLink property.
     *
     * @return
     *     possible object is
     *     {@link Text255Type }
     *
     */
    public Text255Type getDHPCLink() {
        return dhpcLink;
    }

    /**
     * Sets the value of the dhpcLink property.
     *
     * @param value
     *     allowed object is
     *     {@link Text255Type }
     *
     */
    public void setDHPCLink(Text255Type value) {
        this.dhpcLink = value;
    }

    /**
     * Gets the value of the prescriptionNameFamhp property.
     *
     * @return
     *     possible object is
     *     {@link Text255Type }
     *
     */
    public Text255Type getPrescriptionNameFamhp() {
        return prescriptionNameFamhp;
    }

    /**
     * Sets the value of the prescriptionNameFamhp property.
     *
     * @param value
     *     allowed object is
     *     {@link Text255Type }
     *
     */
    public void setPrescriptionNameFamhp(Text255Type value) {
        this.prescriptionNameFamhp = value;
    }

    /**
     * Gets the value of the fmdProductCode property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the fmdProductCode property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getFMDProductCode().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     *
     *
     */
    public List<String> getFMDProductCode() {
        if (fmdProductCode == null) {
            fmdProductCode = new ArrayList<String>();
        }
        return this.fmdProductCode;
    }

    /**
     * Gets the value of the fmdInScope property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isFMDInScope() {
        return fmdInScope;
    }

    /**
     * Sets the value of the fmdInScope property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setFMDInScope(Boolean value) {
        this.fmdInScope = value;
    }

    /**
     * Gets the value of the antiTemperingDevicePresent property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isAntiTemperingDevicePresent() {
        return antiTemperingDevicePresent;
    }

    /**
     * Sets the value of the antiTemperingDevicePresent property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setAntiTemperingDevicePresent(Boolean value) {
        this.antiTemperingDevicePresent = value;
    }

    /**
     * Gets the value of the atc property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the atc property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAtc().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AtcClassificationType }
     *
     *
     */
    public List<AtcClassificationType> getAtc() {
        if (atc == null) {
            atc = new ArrayList<AtcClassificationType>();
        }
        return this.atc;
    }

    /**
     * Gets the value of the deliveryModus property.
     *
     * @return
     *     possible object is
     *     {@link DeliveryModusType }
     *
     */
    public DeliveryModusType getDeliveryModus() {
        return deliveryModus;
    }

    /**
     * Sets the value of the deliveryModus property.
     *
     * @param value
     *     allowed object is
     *     {@link DeliveryModusType }
     *
     */
    public void setDeliveryModus(DeliveryModusType value) {
        this.deliveryModus = value;
    }

    /**
     * Gets the value of the deliveryModusSpecification property.
     *
     * @return
     *     possible object is
     *     {@link DeliveryModusSpecificationType }
     *
     */
    public DeliveryModusSpecificationType getDeliveryModusSpecification() {
        return deliveryModusSpecification;
    }

    /**
     * Sets the value of the deliveryModusSpecification property.
     *
     * @param value
     *     allowed object is
     *     {@link DeliveryModusSpecificationType }
     *
     */
    public void setDeliveryModusSpecification(DeliveryModusSpecificationType value) {
        this.deliveryModusSpecification = value;
    }

    /**
     * Gets the value of the distributorCompany property.
     *
     * @return
     *     possible object is
     *     {@link CompanyFullDataType }
     *
     */
    public CompanyFullDataType getDistributorCompany() {
        return distributorCompany;
    }

    /**
     * Sets the value of the distributorCompany property.
     *
     * @param value
     *     allowed object is
     *     {@link CompanyFullDataType }
     *
     */
    public void setDistributorCompany(CompanyFullDataType value) {
        this.distributorCompany = value;
    }

    /**
     * Gets the value of the singleUse property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isSingleUse() {
        return singleUse;
    }

    /**
     * Sets the value of the singleUse property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setSingleUse(Boolean value) {
        this.singleUse = value;
    }

    /**
     * Gets the value of the speciallyRegulated property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getSpeciallyRegulated() {
        return speciallyRegulated;
    }

    /**
     * Sets the value of the speciallyRegulated property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setSpeciallyRegulated(Integer value) {
        this.speciallyRegulated = value;
    }

    /**
     * Gets the value of the abbreviatedName property.
     *
     * @return
     *     possible object is
     *     {@link Text255Type }
     *
     */
    public Text255Type getAbbreviatedName() {
        return abbreviatedName;
    }

    /**
     * Sets the value of the abbreviatedName property.
     *
     * @param value
     *     allowed object is
     *     {@link Text255Type }
     *
     */
    public void setAbbreviatedName(Text255Type value) {
        this.abbreviatedName = value;
    }

    /**
     * Gets the value of the prescriptionName property.
     *
     * @return
     *     possible object is
     *     {@link Text255Type }
     *
     */
    public Text255Type getPrescriptionName() {
        return prescriptionName;
    }

    /**
     * Sets the value of the prescriptionName property.
     *
     * @param value
     *     allowed object is
     *     {@link Text255Type }
     *
     */
    public void setPrescriptionName(Text255Type value) {
        this.prescriptionName = value;
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
     * Gets the value of the posologyNote property.
     *
     * @return
     *     possible object is
     *     {@link TextType }
     *
     */
    public TextType getPosologyNote() {
        return posologyNote;
    }

    /**
     * Sets the value of the posologyNote property.
     *
     * @param value
     *     allowed object is
     *     {@link TextType }
     *
     */
    public void setPosologyNote(TextType value) {
        this.posologyNote = value;
    }

    /**
     * Gets the value of the crmLink property.
     *
     * @return
     *     possible object is
     *     {@link Text255Type }
     *
     */
    public Text255Type getCrmLink() {
        return crmLink;
    }

    /**
     * Sets the value of the crmLink property.
     *
     * @param value
     *     allowed object is
     *     {@link Text255Type }
     *
     */
    public void setCrmLink(Text255Type value) {
        this.crmLink = value;
    }

    /**
     * Gets the value of the noGenericPrescriptionReason property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the noGenericPrescriptionReason property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNoGenericPrescriptionReason().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link NoGenericPrescriptionReasonType }
     *
     *
     */
    public List<NoGenericPrescriptionReasonType> getNoGenericPrescriptionReason() {
        if (noGenericPrescriptionReason == null) {
            noGenericPrescriptionReason = new ArrayList<NoGenericPrescriptionReasonType>();
        }
        return this.noGenericPrescriptionReason;
    }

    /**
     * Gets the value of the exFactoryPrice property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getExFactoryPrice() {
        return exFactoryPrice;
    }

    /**
     * Sets the value of the exFactoryPrice property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setExFactoryPrice(BigDecimal value) {
        this.exFactoryPrice = value;
    }

    /**
     * Gets the value of the reimbursementCode property.
     *
     * @return
     *     possible object is
     *     {@link Integer }
     *
     */
    public Integer getReimbursementCode() {
        return reimbursementCode;
    }

    /**
     * Sets the value of the reimbursementCode property.
     *
     * @param value
     *     allowed object is
     *     {@link Integer }
     *
     */
    public void setReimbursementCode(Integer value) {
        this.reimbursementCode = value;
    }

    /**
     * Gets the value of the bigPackage property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isBigPackage() {
        return bigPackage;
    }

    /**
     * Sets the value of the bigPackage property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setBigPackage(Boolean value) {
        this.bigPackage = value;
    }

    /**
     * Gets the value of the index property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getIndex() {
        return index;
    }

    /**
     * Sets the value of the index property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setIndex(BigDecimal value) {
        this.index = value;
    }

    /**
     * Gets the value of the definedDailyDose property.
     *
     * @return
     *     possible object is
     *     {@link QuantityType }
     *
     */
    public QuantityType getDefinedDailyDose() {
        return definedDailyDose;
    }

    /**
     * Sets the value of the definedDailyDose property.
     *
     * @param value
     *     allowed object is
     *     {@link QuantityType }
     *
     */
    public void setDefinedDailyDose(QuantityType value) {
        this.definedDailyDose = value;
    }

    /**
     * Gets the value of the officialExFactoryPrice property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getOfficialExFactoryPrice() {
        return officialExFactoryPrice;
    }

    /**
     * Sets the value of the officialExFactoryPrice property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setOfficialExFactoryPrice(BigDecimal value) {
        this.officialExFactoryPrice = value;
    }

    /**
     * Gets the value of the realExFactoryPrice property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getRealExFactoryPrice() {
        return realExFactoryPrice;
    }

    /**
     * Sets the value of the realExFactoryPrice property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setRealExFactoryPrice(BigDecimal value) {
        this.realExFactoryPrice = value;
    }

    /**
     * Gets the value of the pricingInformationDecisionDate property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getPricingInformationDecisionDate() {
        return pricingInformationDecisionDate;
    }

    /**
     * Sets the value of the pricingInformationDecisionDate property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setPricingInformationDecisionDate(XMLGregorianCalendar value) {
        this.pricingInformationDecisionDate = value;
    }

    /**
     * Gets the value of the officialIndex property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getOfficialIndex() {
        return officialIndex;
    }

    /**
     * Sets the value of the officialIndex property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setOfficialIndex(BigDecimal value) {
        this.officialIndex = value;
    }

}
