//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.06.14 at 03:49:33 PM CEST 
//


package org.taktik.icure.be.ehealth.dto.kmehr.v20180901.be.fgov.ehealth.standards.kmehr.cd.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CD-STENT-DEVICEvalues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-STENT-DEVICEvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="bms"/>
 *     &lt;enumeration value="des"/>
 *     &lt;enumeration value="bvs"/>
 *     &lt;enumeration value="other"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CD-STENT-DEVICEvalues")
@XmlEnum
public enum CDSTENTDEVICEvalues {

    @XmlEnumValue("bms")
    BMS("bms"),
    @XmlEnumValue("des")
    DES("des"),
    @XmlEnumValue("bvs")
    BVS("bvs"),
    @XmlEnumValue("other")
    OTHER("other");
    private final String value;

    CDSTENTDEVICEvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDSTENTDEVICEvalues fromValue(String v) {
        for (CDSTENTDEVICEvalues c: CDSTENTDEVICEvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
