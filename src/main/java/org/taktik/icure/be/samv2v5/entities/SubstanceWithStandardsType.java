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

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for SubstanceWithStandardsType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="SubstanceWithStandardsType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:refdata}SubstanceType">
 *       &lt;sequence>
 *         &lt;element name="StandardSubstance" type="{urn:be:fgov:ehealth:samws:v2:export}ExportStandardSubstanceType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubstanceWithStandardsType", propOrder = {
    "standardSubstance"
})
public class SubstanceWithStandardsType
    extends SubstanceType
{

    @XmlElement(name = "StandardSubstance")
    protected List<ExportStandardSubstanceType> standardSubstance;

    /**
     * Gets the value of the standardSubstance property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the standardSubstance property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getStandardSubstance().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ExportStandardSubstanceType }
     *
     *
     */
    public List<ExportStandardSubstanceType> getStandardSubstance() {
        if (standardSubstance == null) {
            standardSubstance = new ArrayList<ExportStandardSubstanceType>();
        }
        return this.standardSubstance;
    }

}
