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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for AmppComponentDataType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AmppComponentDataType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:export}DataPeriodType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}AmppComponentFields"/>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:export}AmppComponentReferenceFields"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AmppComponentDataType", propOrder = {
    "ampcSequenceNr",
    "contentType",
    "contentMultiplier",
    "packSpecification",
    "deviceType",
    "packagingClosure",
    "packagingMaterial",
    "packagingType"
})
public class AmppComponentDataType
    extends DataPeriodType
{

    @XmlElement(name = "AmpcSequenceNr", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Short ampcSequenceNr;
    @XmlElement(name = "ContentType", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common", required = true)
    @XmlSchemaType(name = "string")
    protected ContentTypeType contentType;
    @XmlElement(name = "ContentMultiplier", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected Integer contentMultiplier;
    @XmlElement(name = "PackSpecification", namespace = "urn:be:fgov:ehealth:samws:v2:actual:common")
    protected String packSpecification;
    @XmlElement(name = "DeviceType")
    protected DeviceTypeType deviceType;
    @XmlElement(name = "PackagingClosure")
    protected List<PackagingClosureType> packagingClosure;
    @XmlElement(name = "PackagingMaterial")
    protected List<PackagingMaterialType> packagingMaterial;
    @XmlElement(name = "PackagingType")
    protected PackagingTypeType packagingType;

    /**
     * Gets the value of the ampcSequenceNr property.
     * 
     * @return
     *     possible object is
     *     {@link Short }
     *     
     */
    public Short getAmpcSequenceNr() {
        return ampcSequenceNr;
    }

    /**
     * Sets the value of the ampcSequenceNr property.
     * 
     * @param value
     *     allowed object is
     *     {@link Short }
     *     
     */
    public void setAmpcSequenceNr(Short value) {
        this.ampcSequenceNr = value;
    }

    /**
     * Gets the value of the contentType property.
     * 
     * @return
     *     possible object is
     *     {@link ContentTypeType }
     *     
     */
    public ContentTypeType getContentType() {
        return contentType;
    }

    /**
     * Sets the value of the contentType property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContentTypeType }
     *     
     */
    public void setContentType(ContentTypeType value) {
        this.contentType = value;
    }

    /**
     * Gets the value of the contentMultiplier property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getContentMultiplier() {
        return contentMultiplier;
    }

    /**
     * Sets the value of the contentMultiplier property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setContentMultiplier(Integer value) {
        this.contentMultiplier = value;
    }

    /**
     * Gets the value of the packSpecification property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPackSpecification() {
        return packSpecification;
    }

    /**
     * Sets the value of the packSpecification property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPackSpecification(String value) {
        this.packSpecification = value;
    }

    /**
     * Gets the value of the deviceType property.
     * 
     * @return
     *     possible object is
     *     {@link DeviceTypeType }
     *     
     */
    public DeviceTypeType getDeviceType() {
        return deviceType;
    }

    /**
     * Sets the value of the deviceType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DeviceTypeType }
     *     
     */
    public void setDeviceType(DeviceTypeType value) {
        this.deviceType = value;
    }

    /**
     * Gets the value of the packagingClosure property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the packagingClosure property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPackagingClosure().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PackagingClosureType }
     * 
     * 
     */
    public List<PackagingClosureType> getPackagingClosure() {
        if (packagingClosure == null) {
            packagingClosure = new ArrayList<PackagingClosureType>();
        }
        return this.packagingClosure;
    }

    /**
     * Gets the value of the packagingMaterial property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the packagingMaterial property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPackagingMaterial().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link PackagingMaterialType }
     * 
     * 
     */
    public List<PackagingMaterialType> getPackagingMaterial() {
        if (packagingMaterial == null) {
            packagingMaterial = new ArrayList<PackagingMaterialType>();
        }
        return this.packagingMaterial;
    }

    /**
     * Gets the value of the packagingType property.
     * 
     * @return
     *     possible object is
     *     {@link PackagingTypeType }
     *     
     */
    public PackagingTypeType getPackagingType() {
        return packagingType;
    }

    /**
     * Sets the value of the packagingType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PackagingTypeType }
     *     
     */
    public void setPackagingType(PackagingTypeType value) {
        this.packagingType = value;
    }

}
