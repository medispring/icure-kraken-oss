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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for ExportCompoundingType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="ExportCompoundingType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:export}VersionedExportType">
 *       &lt;sequence>
 *         &lt;element name="CompoundingIngredient" type="{urn:be:fgov:ehealth:samws:v2:export}CompoundingIngredientFullDataType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="CompoundingFormula" type="{urn:be:fgov:ehealth:samws:v2:export}CompoundingFormulaFullDataType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ExportCompoundingType", propOrder = {
        "compoundingIngredients",
        "compoundingFormulas"
})
@XmlRootElement(name = "ExportCompounding")
public class ExportCompounding
        extends VersionedExportType
        implements Serializable {

    private final static long serialVersionUID = 2L;
    @XmlElement(name = "CompoundingIngredient")
    protected List<CompoundingIngredientFullDataType> compoundingIngredients;
    @XmlElement(name = "CompoundingFormula")
    protected List<CompoundingFormulaFullDataType> compoundingFormulas;

    /**
     * Gets the value of the compoundingIngredients property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the compoundingIngredients property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompoundingIngredients().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CompoundingIngredientFullDataType }
     */
    public List<CompoundingIngredientFullDataType> getCompoundingIngredients() {
        if (compoundingIngredients == null) {
            compoundingIngredients = new ArrayList<CompoundingIngredientFullDataType>();
        }
        return this.compoundingIngredients;
    }

    /**
     * Gets the value of the compoundingFormulas property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the compoundingFormulas property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCompoundingFormulas().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CompoundingFormulaFullDataType }
     */
    public List<CompoundingFormulaFullDataType> getCompoundingFormulas() {
        if (compoundingFormulas == null) {
            compoundingFormulas = new ArrayList<CompoundingFormulaFullDataType>();
        }
        return this.compoundingFormulas;
    }

}
