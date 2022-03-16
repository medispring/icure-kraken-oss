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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VirtualIngredientFullDataType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="VirtualIngredientFullDataType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:virtual:common}VirtualIngredientKeyType">
 *       &lt;sequence>
 *         &lt;element name="Data" type="{urn:be:fgov:ehealth:samws:v2:export}VirtualIngredientDataType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="RealVirtualIngredient" type="{urn:be:fgov:ehealth:samws:v2:export}RealVirtualIngredientFullDataType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VirtualIngredientFullDataType", propOrder = {
    "data",
    "realVirtualIngredient"
})
public class VirtualIngredientFullDataType
    extends VirtualIngredientKeyType
{

    @XmlElement(name = "Data")
    protected List<VirtualIngredientDataType> data;
    @XmlElement(name = "RealVirtualIngredient")
    protected List<RealVirtualIngredientFullDataType> realVirtualIngredient;

    /**
     * Gets the value of the data property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the data property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getData().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link VirtualIngredientDataType }
     *
     *
     */
    public List<VirtualIngredientDataType> getData() {
        if (data == null) {
            data = new ArrayList<VirtualIngredientDataType>();
        }
        return this.data;
    }

    /**
     * Gets the value of the realVirtualIngredient property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the realVirtualIngredient property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRealVirtualIngredient().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link RealVirtualIngredientFullDataType }
     *
     *
     */
    public List<RealVirtualIngredientFullDataType> getRealVirtualIngredient() {
        if (realVirtualIngredient == null) {
            realVirtualIngredient = new ArrayList<RealVirtualIngredientFullDataType>();
        }
        return this.realVirtualIngredient;
    }

}
