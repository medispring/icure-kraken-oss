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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for ChangeAmpFamhpType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ChangeAmpFamhpType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:actual:common}AmpKeyType">
 *       &lt;sequence>
 *         &lt;sequence minOccurs="0">
 *           &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}AmpFamhpFields"/>
 *           &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}AmpFamhpReferences"/>
 *         &lt;/sequence>
 *         &lt;element name="AmpComponent" type="{urn:be:fgov:ehealth:samws:v2:actual:common}ChangeAmpComponentFamhpType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="Ampp" type="{urn:be:fgov:ehealth:samws:v2:actual:common}ChangeAmppFamhpType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:be:fgov:ehealth:samws:v2:core}changeNoChangeMetadata"/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ChangeAmpFamhpType", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common", propOrder = {
    "officialName",
    "status",
    "name",
    "blackTriangle",
    "medicineType",
    "prescriptionNameFamhp",
    "companyActorNr",
    "ampComponent",
    "ampp"
})
public class ChangeAmpFamhpType
    extends AmpKeyType
{

    @XmlElement(name = "OfficialName")
    protected String officialName;
    @XmlElement(name = "Status")
    @XmlSchemaType(name = "string")
    protected AmpStatusType status;
    @XmlElement(name = "Name")
    protected Text255Type name;
    @XmlElement(name = "BlackTriangle")
    protected Boolean blackTriangle;
    @XmlElement(name = "MedicineType")
    @XmlSchemaType(name = "string")
    protected MedicineTypeType medicineType;
    @XmlElement(name = "PrescriptionNameFamhp")
    protected Text255Type prescriptionNameFamhp;
    @XmlElement(name = "CompanyActorNr")
    protected String companyActorNr;
    @XmlElement(name = "AmpComponent")
    protected List<ChangeAmpComponentFamhpType> ampComponent;
    @XmlElement(name = "Ampp")
    protected List<ChangeAmppFamhpType> ampp;
    @XmlAttribute(name = "action", required = true)
    protected ChangeNoChangeActionType action;
    @XmlAttribute(name = "from")
    protected XMLGregorianCalendar from;
    @XmlAttribute(name = "to")
    protected XMLGregorianCalendar to;

    /**
     * Gets the value of the officialName property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getOfficialName() {
        return officialName;
    }

    /**
     * Sets the value of the officialName property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setOfficialName(String value) {
        this.officialName = value;
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
     * Gets the value of the blackTriangle property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isBlackTriangle() {
        return blackTriangle;
    }

    /**
     * Sets the value of the blackTriangle property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setBlackTriangle(Boolean value) {
        this.blackTriangle = value;
    }

    /**
     * Gets the value of the medicineType property.
     *
     * @return
     *     possible object is
     *     {@link MedicineTypeType }
     *
     */
    public MedicineTypeType getMedicineType() {
        return medicineType;
    }

    /**
     * Sets the value of the medicineType property.
     *
     * @param value
     *     allowed object is
     *     {@link MedicineTypeType }
     *
     */
    public void setMedicineType(MedicineTypeType value) {
        this.medicineType = value;
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
     * Gets the value of the companyActorNr property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getCompanyActorNr() {
        return companyActorNr;
    }

    /**
     * Sets the value of the companyActorNr property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setCompanyActorNr(String value) {
        this.companyActorNr = value;
    }

    /**
     * Gets the value of the ampComponent property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ampComponent property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAmpComponent().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ChangeAmpComponentFamhpType }
     *
     *
     */
    public List<ChangeAmpComponentFamhpType> getAmpComponent() {
        if (ampComponent == null) {
            ampComponent = new ArrayList<ChangeAmpComponentFamhpType>();
        }
        return this.ampComponent;
    }

    /**
     * Gets the value of the ampp property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ampp property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAmpp().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ChangeAmppFamhpType }
     *
     *
     */
    public List<ChangeAmppFamhpType> getAmpp() {
        if (ampp == null) {
            ampp = new ArrayList<ChangeAmppFamhpType>();
        }
        return this.ampp;
    }

    /**
     * Gets the value of the action property.
     *
     * @return
     *     possible object is
     *     {@link ChangeNoChangeActionType }
     *
     */
    public ChangeNoChangeActionType getAction() {
        return action;
    }

    /**
     * Sets the value of the action property.
     *
     * @param value
     *     allowed object is
     *     {@link ChangeNoChangeActionType }
     *
     */
    public void setAction(ChangeNoChangeActionType value) {
        this.action = value;
    }

    /**
     * Gets the value of the from property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getFrom() {
        return from;
    }

    /**
     * Sets the value of the from property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setFrom(XMLGregorianCalendar value) {
        this.from = value;
    }

    /**
     * Gets the value of the to property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getTo() {
        return to;
    }

    /**
     * Sets the value of the to property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setTo(XMLGregorianCalendar value) {
        this.to = value;
    }

}
