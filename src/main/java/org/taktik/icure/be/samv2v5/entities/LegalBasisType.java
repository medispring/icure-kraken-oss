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
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for LegalBasisType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="LegalBasisType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:reimbursementlaw:submit}LegalBasisKeyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:reimbursementlaw:submit}LegalBasisFields"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LegalBasisType", namespace = "urn:be:fgov:ehealth:samws:v2:reimbursementlaw:submit", propOrder = {
    "title",
    "type",
    "effectiveOn"
})
@XmlSeeAlso({
    SubmitLegalBasisType.class
})
public class LegalBasisType
    extends LegalBasisKeyType
{

    @XmlElement(name = "Title")
    protected Text255Type title;
    @XmlElement(name = "Type")
    @XmlSchemaType(name = "string")
    protected LegalBasisTypeType type;
    @XmlElement(name = "EffectiveOn")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar effectiveOn;

    /**
     * Gets the value of the title property.
     * 
     * @return
     *     possible object is
     *     {@link Text255Type }
     *     
     */
    public Text255Type getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     * 
     * @param value
     *     allowed object is
     *     {@link Text255Type }
     *     
     */
    public void setTitle(Text255Type value) {
        this.title = value;
    }

    /**
     * Gets the value of the type property.
     * 
     * @return
     *     possible object is
     *     {@link LegalBasisTypeType }
     *     
     */
    public LegalBasisTypeType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     * 
     * @param value
     *     allowed object is
     *     {@link LegalBasisTypeType }
     *     
     */
    public void setType(LegalBasisTypeType value) {
        this.type = value;
    }

    /**
     * Gets the value of the effectiveOn property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEffectiveOn() {
        return effectiveOn;
    }

    /**
     * Sets the value of the effectiveOn property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEffectiveOn(XMLGregorianCalendar value) {
        this.effectiveOn = value;
    }

}
