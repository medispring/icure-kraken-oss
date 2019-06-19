//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.06.14 at 03:48:52 PM CEST 
//


package org.taktik.icure.be.ehealth.dto.kmehr.v20100901.be.fgov.ehealth.standards.kmehr.cd.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CD-LNKvalues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-LNKvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="multimedia"/>
 *     &lt;enumeration value="isaconsequenceof"/>
 *     &lt;enumeration value="isanewversionof"/>
 *     &lt;enumeration value="isareplyto"/>
 *     &lt;enumeration value="thumbnail"/>
 *     &lt;enumeration value="isachildof"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CD-LNKvalues")
@XmlEnum
public enum CDLNKvalues {

    @XmlEnumValue("multimedia")
    MULTIMEDIA("multimedia"),
    @XmlEnumValue("isaconsequenceof")
    ISACONSEQUENCEOF("isaconsequenceof"),
    @XmlEnumValue("isanewversionof")
    ISANEWVERSIONOF("isanewversionof"),
    @XmlEnumValue("isareplyto")
    ISAREPLYTO("isareplyto"),
    @XmlEnumValue("thumbnail")
    THUMBNAIL("thumbnail"),
    @XmlEnumValue("isachildof")
    ISACHILDOF("isachildof");
    private final String value;

    CDLNKvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDLNKvalues fromValue(String v) {
        for (CDLNKvalues c: CDLNKvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
