//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2019.05.22 at 08:11:32 PM CEST
//


package org.taktik.icure.be.ehealth.samws.v2.company.submit;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CompanyLanguageType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CompanyLanguageType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="FR"/>
 *     &lt;enumeration value="NL"/>
 *     &lt;enumeration value="FR/NL"/>
 *     &lt;enumeration value="DE"/>
 *     &lt;enumeration value="EN"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "CompanyLanguageType")
@XmlEnum
public enum CompanyLanguageType {

    FR("FR"),
    NL("NL"),
    @XmlEnumValue("FR/NL")
    FR_NL("FR/NL"),
    DE("DE"),
    EN("EN");
    private final String value;

    CompanyLanguageType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CompanyLanguageType fromValue(String v) {
        for (CompanyLanguageType c: CompanyLanguageType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
