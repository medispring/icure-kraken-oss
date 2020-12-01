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


package org.taktik.icure.be.ehealth.samws.v2.actual.common;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.taktik.icure.be.ehealth.samws.v2.core.IngredientTypeType;
import org.taktik.icure.be.ehealth.samws.v2.core.QuantityType;


/**
 * <p>Java class for RealActualIngredientEquivalentType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RealActualIngredientEquivalentType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:actual:common}RealActualIngredientEquivalentKeyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}RealActualIngredientEquivalentFields"/>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:actual:common}RealActualIngredientEquivalentReferences"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RealActualIngredientEquivalentType", propOrder = {
    "type",
    "knownEffect",
    "strengthDescription",
    "strength",
    "substanceCode"
})
@XmlSeeAlso({
    AddRealActualIngredientEquivalentType.class,
    ChangeRealActualIngredientEquivalentType.class
})
public class RealActualIngredientEquivalentType
    extends RealActualIngredientEquivalentKeyType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(name = "Type", required = true)
    @XmlSchemaType(name = "string")
    protected IngredientTypeType type;
    @XmlElement(name = "KnownEffect")
    protected Boolean knownEffect;
    @XmlElement(name = "StrengthDescription")
    protected String strengthDescription;
    @XmlElement(name = "Strength")
    protected QuantityType strength;
    @XmlElement(name = "SubstanceCode", required = true)
    protected String substanceCode;

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

}
