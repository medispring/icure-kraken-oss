//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2019.05.22 at 08:11:32 PM CEST
//


package org.taktik.icure.be.ehealth.samws.v2.actual.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.taktik.icure.be.ehealth.samws.v2.core.ChangeNoChangeActionType;
import org.taktik.icure.be.ehealth.samws.v2.core.IngredientTypeType;
import org.taktik.icure.be.ehealth.samws.v2.core.QuantityType;


/**
 * <p>Java class for ChangeRealActualIngredientType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ChangeRealActualIngredientType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:actual:common}RealActualIngredientKeyType">
 *       &lt;sequence>
 *         &lt;sequence minOccurs="0">
 *           &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}RealActualIngredientFields"/>
 *           &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}RealActualIngredientReferences"/>
 *         &lt;/sequence>
 *         &lt;element name="RealActualIngredientEquivalent" type="{urn:be:fgov:ehealth:samws:v2:actual:common}ChangeRealActualIngredientEquivalentType" maxOccurs="unbounded" minOccurs="0"/>
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
@XmlType(name = "ChangeRealActualIngredientType", propOrder = {
    "type",
    "knownEffect",
    "strengthDescription",
    "strength",
    "additionalInformation",
    "substanceCode",
    "realActualIngredientEquivalents"
})
public class ChangeRealActualIngredientType
    extends RealActualIngredientKeyType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(name = "Type")
    @XmlSchemaType(name = "string")
    protected IngredientTypeType type;
    @XmlElement(name = "KnownEffect")
    protected Boolean knownEffect;
    @XmlElement(name = "StrengthDescription")
    protected String strengthDescription;
    @XmlElement(name = "Strength")
    protected QuantityType strength;
    @XmlElement(name = "AdditionalInformation")
    protected String additionalInformation;
    @XmlElement(name = "SubstanceCode")
    protected String substanceCode;
    @XmlElement(name = "RealActualIngredientEquivalent")
    protected List<ChangeRealActualIngredientEquivalentType> realActualIngredientEquivalents;
    @XmlAttribute(name = "action", required = true)
    protected ChangeNoChangeActionType action;
    @XmlAttribute(name = "from")
    protected XMLGregorianCalendar from;
    @XmlAttribute(name = "to")
    protected XMLGregorianCalendar to;

    /**
     * Gets the value of the type property.
     *
     * @return
     *     possible object is
     *     {@link IngredientTypeType }
     *
     */
    public IngredientTypeType getType() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value
     *     allowed object is
     *     {@link IngredientTypeType }
     *
     */
    public void setType(IngredientTypeType value) {
        this.type = value;
    }

    /**
     * Gets the value of the knownEffect property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isKnownEffect() {
        return knownEffect;
    }

    /**
     * Sets the value of the knownEffect property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setKnownEffect(Boolean value) {
        this.knownEffect = value;
    }

    /**
     * Gets the value of the strengthDescription property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getStrengthDescription() {
        return strengthDescription;
    }

    /**
     * Sets the value of the strengthDescription property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setStrengthDescription(String value) {
        this.strengthDescription = value;
    }

    /**
     * Gets the value of the strength property.
     *
     * @return
     *     possible object is
     *     {@link QuantityType }
     *
     */
    public QuantityType getStrength() {
        return strength;
    }

    /**
     * Sets the value of the strength property.
     *
     * @param value
     *     allowed object is
     *     {@link QuantityType }
     *
     */
    public void setStrength(QuantityType value) {
        this.strength = value;
    }

    /**
     * Gets the value of the additionalInformation property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * Sets the value of the additionalInformation property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setAdditionalInformation(String value) {
        this.additionalInformation = value;
    }

    /**
     * Gets the value of the substanceCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSubstanceCode() {
        return substanceCode;
    }

    /**
     * Sets the value of the substanceCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSubstanceCode(String value) {
        this.substanceCode = value;
    }

    /**
     * Gets the value of the realActualIngredientEquivalents property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the realActualIngredientEquivalents property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRealActualIngredientEquivalents().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ChangeRealActualIngredientEquivalentType }
     *
     *
     */
    public List<ChangeRealActualIngredientEquivalentType> getRealActualIngredientEquivalents() {
        if (realActualIngredientEquivalents == null) {
            realActualIngredientEquivalents = new ArrayList<ChangeRealActualIngredientEquivalentType>();
        }
        return this.realActualIngredientEquivalents;
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
