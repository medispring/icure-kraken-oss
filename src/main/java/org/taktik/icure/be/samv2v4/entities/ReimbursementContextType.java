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
// Generated on: 2020.05.12 at 04:36:37 PM CEST
//


package org.taktik.icure.be.samv2v4.entities;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for ReimbursementContextType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ReimbursementContextType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:reimbursement:submit}ReimbursementContextKeyType">
 *       &lt;sequence minOccurs="0">
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:reimbursement:submit}ReimbursementContextFields"/>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:reimbursement:submit}ReimbursementContextReferences"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ReimbursementContextType", namespace = "urn:be:fgov:ehealth:samws:v2:reimbursement:submit", propOrder = {
    "multiple",
    "temporary",
    "reference",
    "flatRateSystem",
    "reimbursementBasePrice",
    "referenceBasePrice",
    "copaymentSupplement",
    "pricingUnit",
    "pricingSlice",
    "reimbursementCriterionCategory",
    "reimbursementCriterionCode"
})
@XmlSeeAlso({
    SubmitReimbursementContextType.class
})
public class ReimbursementContextType
    extends ReimbursementContextKeyType
{

    @XmlElement(name = "Multiple")
    @XmlSchemaType(name = "string")
    protected MultipleType multiple;
    @XmlElement(name = "Temporary")
    protected Boolean temporary;
    @XmlElement(name = "Reference")
    protected Boolean reference;
    @XmlElement(name = "FlatRateSystem")
    protected Boolean flatRateSystem;
    @XmlElement(name = "ReimbursementBasePrice")
    protected BigDecimal reimbursementBasePrice;
    @XmlElement(name = "ReferenceBasePrice")
    protected BigDecimal referenceBasePrice;
    @XmlElement(name = "CopaymentSupplement")
    protected BigDecimal copaymentSupplement;
    @XmlElement(name = "PricingUnit")
    protected PricingUnitType pricingUnit;
    @XmlElement(name = "PricingSlice")
    protected PricingUnitType pricingSlice;
    @XmlElement(name = "ReimbursementCriterionCategory")
    protected String reimbursementCriterionCategory;
    @XmlElement(name = "ReimbursementCriterionCode")
    protected String reimbursementCriterionCode;

    /**
     * Gets the value of the multiple property.
     *
     * @return
     *     possible object is
     *     {@link MultipleType }
     *
     */
    public MultipleType getMultiple() {
        return multiple;
    }

    /**
     * Sets the value of the multiple property.
     *
     * @param value
     *     allowed object is
     *     {@link MultipleType }
     *
     */
    public void setMultiple(MultipleType value) {
        this.multiple = value;
    }

    /**
     * Gets the value of the temporary property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isTemporary() {
        return temporary;
    }

    /**
     * Sets the value of the temporary property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setTemporary(Boolean value) {
        this.temporary = value;
    }

    /**
     * Gets the value of the reference property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isReference() {
        return reference;
    }

    /**
     * Sets the value of the reference property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setReference(Boolean value) {
        this.reference = value;
    }

    /**
     * Gets the value of the flatRateSystem property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isFlatRateSystem() {
        return flatRateSystem;
    }

    /**
     * Sets the value of the flatRateSystem property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setFlatRateSystem(Boolean value) {
        this.flatRateSystem = value;
    }

    /**
     * Gets the value of the reimbursementBasePrice property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getReimbursementBasePrice() {
        return reimbursementBasePrice;
    }

    /**
     * Sets the value of the reimbursementBasePrice property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setReimbursementBasePrice(BigDecimal value) {
        this.reimbursementBasePrice = value;
    }

    /**
     * Gets the value of the referenceBasePrice property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getReferenceBasePrice() {
        return referenceBasePrice;
    }

    /**
     * Sets the value of the referenceBasePrice property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setReferenceBasePrice(BigDecimal value) {
        this.referenceBasePrice = value;
    }

    /**
     * Gets the value of the copaymentSupplement property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getCopaymentSupplement() {
        return copaymentSupplement;
    }

    /**
     * Sets the value of the copaymentSupplement property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setCopaymentSupplement(BigDecimal value) {
        this.copaymentSupplement = value;
    }

    /**
     * Gets the value of the pricingUnit property.
     *
     * @return
     *     possible object is
     *     {@link PricingUnitType }
     *
     */
    public PricingUnitType getPricingUnit() {
        return pricingUnit;
    }

    /**
     * Sets the value of the pricingUnit property.
     *
     * @param value
     *     allowed object is
     *     {@link PricingUnitType }
     *
     */
    public void setPricingUnit(PricingUnitType value) {
        this.pricingUnit = value;
    }

    /**
     * Gets the value of the pricingSlice property.
     *
     * @return
     *     possible object is
     *     {@link PricingUnitType }
     *
     */
    public PricingUnitType getPricingSlice() {
        return pricingSlice;
    }

    /**
     * Sets the value of the pricingSlice property.
     *
     * @param value
     *     allowed object is
     *     {@link PricingUnitType }
     *
     */
    public void setPricingSlice(PricingUnitType value) {
        this.pricingSlice = value;
    }

    /**
     * Gets the value of the reimbursementCriterionCategory property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getReimbursementCriterionCategory() {
        return reimbursementCriterionCategory;
    }

    /**
     * Sets the value of the reimbursementCriterionCategory property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setReimbursementCriterionCategory(String value) {
        this.reimbursementCriterionCategory = value;
    }

    /**
     * Gets the value of the reimbursementCriterionCode property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getReimbursementCriterionCode() {
        return reimbursementCriterionCode;
    }

    /**
     * Sets the value of the reimbursementCriterionCode property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setReimbursementCriterionCode(String value) {
        this.reimbursementCriterionCode = value;
    }

}
