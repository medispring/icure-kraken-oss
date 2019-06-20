//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.06.14 at 03:49:37 PM CEST 
//


package org.taktik.icure.be.ehealth.dto.kmehr.v20180301.be.fgov.ehealth.standards.kmehr.cd.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CD-TRANSACTION-MAAvalues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-TRANSACTION-MAAvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="agreementrequest"/>
 *     &lt;enumeration value="agreementresponse"/>
 *     &lt;enumeration value="freeappendix"/>
 *     &lt;enumeration value="reglementaryappendix"/>
 *     &lt;enumeration value="consultationrequest"/>
 *     &lt;enumeration value="consultationresponse"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CD-TRANSACTION-MAAvalues")
@XmlEnum
public enum CDTRANSACTIONMAAvalues {

    @XmlEnumValue("agreementrequest")
    AGREEMENTREQUEST("agreementrequest"),
    @XmlEnumValue("agreementresponse")
    AGREEMENTRESPONSE("agreementresponse"),
    @XmlEnumValue("freeappendix")
    FREEAPPENDIX("freeappendix"),
    @XmlEnumValue("reglementaryappendix")
    REGLEMENTARYAPPENDIX("reglementaryappendix"),
    @XmlEnumValue("consultationrequest")
    CONSULTATIONREQUEST("consultationrequest"),
    @XmlEnumValue("consultationresponse")
    CONSULTATIONRESPONSE("consultationresponse");
    private final String value;

    CDTRANSACTIONMAAvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDTRANSACTIONMAAvalues fromValue(String v) {
        for (CDTRANSACTIONMAAvalues c: CDTRANSACTIONMAAvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
