//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.05.12 at 04:36:37 PM CEST 
//


package org.taktik.icure.be.samv2v4.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CompoundingIngredientKeyType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="CompoundingIngredientKeyType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="code" use="required" type="{urn:be:fgov:ehealth:samws:v2:core}DmppCodeType" />
 *       &lt;attribute name="codeType" type="{urn:be:fgov:ehealth:samws:v2:core}DmppCodeTypeType" default="CNK" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "CompoundingIngredientKeyType", namespace = "urn:be:fgov:ehealth:samws:v2:compounding:common")
@XmlSeeAlso({
    CompoundingIngredientFullDataType.class,
    CompoundingIngredientType.class,
    RemoveCompoundingIngredientType.class
})
public class CompoundingIngredientKeyType {

    @XmlAttribute(name = "code", required = true)
    protected String code;
    @XmlAttribute(name = "codeType")
    protected DmppCodeTypeType codeType;

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCode(String value) {
        this.code = value;
    }

    /**
     * Gets the value of the codeType property.
     * 
     * @return
     *     possible object is
     *     {@link DmppCodeTypeType }
     *     
     */
    public DmppCodeTypeType getCodeType() {
        if (codeType == null) {
            return DmppCodeTypeType.CNK;
        } else {
            return codeType;
        }
    }

    /**
     * Sets the value of the codeType property.
     * 
     * @param value
     *     allowed object is
     *     {@link DmppCodeTypeType }
     *     
     */
    public void setCodeType(DmppCodeTypeType value) {
        this.codeType = value;
    }

}
