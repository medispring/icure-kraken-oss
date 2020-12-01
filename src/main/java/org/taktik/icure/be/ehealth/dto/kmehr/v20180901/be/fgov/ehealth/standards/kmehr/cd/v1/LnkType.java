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
// Generated on: 2019.06.14 at 03:49:33 PM CEST
//


package org.taktik.icure.be.ehealth.dto.kmehr.v20180901.be.fgov.ehealth.standards.kmehr.cd.v1;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;


/**
 * <p>Java class for lnkType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="lnkType">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>base64Binary">
 *       &lt;attribute name="TYPE" use="required" type="{http://www.ehealth.fgov.be/standards/kmehr/cd/v1}CD-LNKvalues" />
 *       &lt;attribute name="MEDIATYPE" type="{http://www.ehealth.fgov.be/standards/kmehr/cd/v1}CD-MEDIATYPEvalues" />
 *       &lt;attribute name="URL" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="SIZE" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "lnkType", propOrder = {
    "value"
})
public class LnkType implements Serializable
{

    private final static long serialVersionUID = 20180901L;
    @XmlValue
    protected byte[] value;
    @XmlAttribute(name = "TYPE", required = true)
    protected CDLNKvalues type;
    @XmlAttribute(name = "MEDIATYPE")
    protected CDMEDIATYPEvalues mediatype;
    @XmlAttribute(name = "URL")
    protected String url;
    @XmlAttribute(name = "SIZE")
    protected String size;

    /**
     * Gets the value of the value property.
     *
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     *
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setValue(byte[] value) {
        this.value = value;
    }

    /**
     * Gets the value of the type property.
     *
     * @return
     *     possible object is
     *     {@link CDLNKvalues }
     *
     */
    public CDLNKvalues getTYPE() {
        return type;
    }

    /**
     * Sets the value of the type property.
     *
     * @param value
     *     allowed object is
     *     {@link CDLNKvalues }
     *
     */
    public void setTYPE(CDLNKvalues value) {
        this.type = value;
    }

    /**
     * Gets the value of the mediatype property.
     *
     * @return
     *     possible object is
     *     {@link CDMEDIATYPEvalues }
     *
     */
    public CDMEDIATYPEvalues getMEDIATYPE() {
        return mediatype;
    }

    /**
     * Sets the value of the mediatype property.
     *
     * @param value
     *     allowed object is
     *     {@link CDMEDIATYPEvalues }
     *
     */
    public void setMEDIATYPE(CDMEDIATYPEvalues value) {
        this.mediatype = value;
    }

    /**
     * Gets the value of the url property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getURL() {
        return url;
    }

    /**
     * Sets the value of the url property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setURL(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the size property.
     *
     * @return
     *     possible object is
     *     {@link String }
     *
     */
    public String getSIZE() {
        return size;
    }

    /**
     * Sets the value of the size property.
     *
     * @param value
     *     allowed object is
     *     {@link String }
     *
     */
    public void setSIZE(String value) {
        this.size = value;
    }

}
