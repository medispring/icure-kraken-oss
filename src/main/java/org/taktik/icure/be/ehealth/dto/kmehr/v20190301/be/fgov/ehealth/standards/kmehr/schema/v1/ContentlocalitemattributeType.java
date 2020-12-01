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
// Generated on: 2019.06.14 at 03:49:41 PM CEST
//


package org.taktik.icure.be.ehealth.dto.kmehr.v20190301.be.fgov.ehealth.standards.kmehr.schema.v1;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;
import org.taktik.icure.be.ehealth.dto.kmehr.v20190301.be.fgov.ehealth.standards.kmehr.cd.v1.CDCONTENT;
import org.taktik.icure.be.ehealth.dto.kmehr.v20190301.be.fgov.ehealth.standards.kmehr.dt.v1.TextType;
import org.taktik.icure.be.ehealth.dto.kmehr.v20190301.be.fgov.ehealth.standards.kmehr.id.v1.IDKMEHR;


/**
 * <p>Java class for contentlocalitemattributeType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="contentlocalitemattributeType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.ehealth.fgov.be/standards/kmehr/id/v1}ID-KMEHR" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="cd" type="{http://www.ehealth.fgov.be/standards/kmehr/cd/v1}CD-CONTENT" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;choice>
 *           &lt;element name="decimal" type="{http://www.w3.org/2001/XMLSchema}decimal"/>
 *           &lt;element name="boolean" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *           &lt;element name="text" type="{http://www.ehealth.fgov.be/standards/kmehr/dt/v1}textType"/>
 *           &lt;sequence>
 *             &lt;element name="date" type="{http://www.w3.org/2001/XMLSchema}date" minOccurs="0"/>
 *             &lt;element name="time" type="{http://www.w3.org/2001/XMLSchema}time" minOccurs="0"/>
 *           &lt;/sequence>
 *           &lt;element name="hcparty" type="{http://www.ehealth.fgov.be/standards/kmehr/schema/v1}hcpartyType"/>
 *           &lt;element name="person" type="{http://www.ehealth.fgov.be/standards/kmehr/schema/v1}personType"/>
 *           &lt;element name="lnk" type="{http://www.w3.org/2001/XMLSchema}anyType" maxOccurs="unbounded"/>
 *         &lt;/choice>
 *         &lt;element name="unit" type="{http://www.ehealth.fgov.be/standards/kmehr/schema/v1}unitType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contentlocalitemattributeType", propOrder = {
    "ids",
    "cds",
    "lnks",
    "person",
    "hcparty",
    "date",
    "time",
    "text",
    "_boolean",
    "decimal",
    "unit"
})
public class ContentlocalitemattributeType
    implements Serializable
{

    private final static long serialVersionUID = 20190301L;
    @XmlElement(name = "id")
    protected List<IDKMEHR> ids;
    @XmlElement(name = "cd")
    protected List<CDCONTENT> cds;
    @XmlElement(name = "lnk")
    protected List<Object> lnks;
    protected PersonType person;
    protected HcpartyType hcparty;
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar date;
    @XmlSchemaType(name = "time")
    protected XMLGregorianCalendar time;
    protected TextType text;
    @XmlElement(name = "boolean")
    protected Boolean _boolean;
    protected BigDecimal decimal;
    protected UnitType unit;

    /**
     * Gets the value of the ids property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the ids property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIds().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link IDKMEHR }
     *
     *
     */
    public List<IDKMEHR> getIds() {
        if (ids == null) {
            ids = new ArrayList<IDKMEHR>();
        }
        return this.ids;
    }

    /**
     * Gets the value of the cds property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cds property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCds().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CDCONTENT }
     *
     *
     */
    public List<CDCONTENT> getCds() {
        if (cds == null) {
            cds = new ArrayList<CDCONTENT>();
        }
        return this.cds;
    }

    /**
     * Gets the value of the lnks property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lnks property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLnks().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     *
     *
     */
    public List<Object> getLnks() {
        if (lnks == null) {
            lnks = new ArrayList<Object>();
        }
        return this.lnks;
    }

    /**
     * Gets the value of the person property.
     *
     * @return
     *     possible object is
     *     {@link PersonType }
     *
     */
    public PersonType getPerson() {
        return person;
    }

    /**
     * Sets the value of the person property.
     *
     * @param value
     *     allowed object is
     *     {@link PersonType }
     *
     */
    public void setPerson(PersonType value) {
        this.person = value;
    }

    /**
     * Gets the value of the hcparty property.
     *
     * @return
     *     possible object is
     *     {@link HcpartyType }
     *
     */
    public HcpartyType getHcparty() {
        return hcparty;
    }

    /**
     * Sets the value of the hcparty property.
     *
     * @param value
     *     allowed object is
     *     {@link HcpartyType }
     *
     */
    public void setHcparty(HcpartyType value) {
        this.hcparty = value;
    }

    /**
     * Gets the value of the date property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getDate() {
        return date;
    }

    /**
     * Sets the value of the date property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setDate(XMLGregorianCalendar value) {
        this.date = value;
    }

    /**
     * Gets the value of the time property.
     *
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public XMLGregorianCalendar getTime() {
        return time;
    }

    /**
     * Sets the value of the time property.
     *
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *
     */
    public void setTime(XMLGregorianCalendar value) {
        this.time = value;
    }

    /**
     * Gets the value of the text property.
     *
     * @return
     *     possible object is
     *     {@link TextType }
     *
     */
    public TextType getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     *
     * @param value
     *     allowed object is
     *     {@link TextType }
     *
     */
    public void setText(TextType value) {
        this.text = value;
    }

    /**
     * Gets the value of the boolean property.
     *
     * @return
     *     possible object is
     *     {@link Boolean }
     *
     */
    public Boolean isBoolean() {
        return _boolean;
    }

    /**
     * Sets the value of the boolean property.
     *
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *
     */
    public void setBoolean(Boolean value) {
        this._boolean = value;
    }

    /**
     * Gets the value of the decimal property.
     *
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *
     */
    public BigDecimal getDecimal() {
        return decimal;
    }

    /**
     * Sets the value of the decimal property.
     *
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *
     */
    public void setDecimal(BigDecimal value) {
        this.decimal = value;
    }

    /**
     * Gets the value of the unit property.
     *
     * @return
     *     possible object is
     *     {@link UnitType }
     *
     */
    public UnitType getUnit() {
        return unit;
    }

    /**
     * Sets the value of the unit property.
     *
     * @param value
     *     allowed object is
     *     {@link UnitType }
     *
     */
    public void setUnit(UnitType value) {
        this.unit = value;
    }

}
