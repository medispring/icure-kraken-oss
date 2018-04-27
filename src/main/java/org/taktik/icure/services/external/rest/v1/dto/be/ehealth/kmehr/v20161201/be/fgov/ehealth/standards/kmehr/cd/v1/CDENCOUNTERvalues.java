/*
 * Copyright (C) 2018 Taktik SA
 *
 * This file is part of iCureBackend.
 *
 * iCureBackend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * iCureBackend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with iCureBackend.  If not, see <http://www.gnu.org/licenses/>.
 */

//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.03.09 at 09:24:53 AM CET 
//


package org.taktik.icure.services.external.rest.v1.dto.be.ehealth.kmehr.v20161201.be.fgov.ehealth.standards.kmehr.cd.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CD-ENCOUNTERvalues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-ENCOUNTERvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="homevisit"/>
 *     &lt;enumeration value="consultation"/>
 *     &lt;enumeration value="telephonicconsultation"/>
 *     &lt;enumeration value="emergency"/>
 *     &lt;enumeration value="hospital"/>
 *     &lt;enumeration value="oneday"/>
 *     &lt;enumeration value="technical"/>
 *     &lt;enumeration value="resthomevisit"/>
 *     &lt;enumeration value="consult"/>
 *     &lt;enumeration value="multidisciplinaryconsult"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CD-ENCOUNTERvalues")
@XmlEnum
public enum CDENCOUNTERvalues {

    @XmlEnumValue("homevisit")
    HOMEVISIT("homevisit"),
    @XmlEnumValue("consultation")
    CONSULTATION("consultation"),
    @XmlEnumValue("telephonicconsultation")
    TELEPHONICCONSULTATION("telephonicconsultation"),
    @XmlEnumValue("emergency")
    EMERGENCY("emergency"),
    @XmlEnumValue("hospital")
    HOSPITAL("hospital"),
    @XmlEnumValue("oneday")
    ONEDAY("oneday"),
    @XmlEnumValue("technical")
    TECHNICAL("technical"),
    @XmlEnumValue("resthomevisit")
    RESTHOMEVISIT("resthomevisit"),
    @XmlEnumValue("consult")
    CONSULT("consult"),
    @XmlEnumValue("multidisciplinaryconsult")
    MULTIDISCIPLINARYCONSULT("multidisciplinaryconsult");
    private final String value;

    CDENCOUNTERvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDENCOUNTERvalues fromValue(String v) {
        for (CDENCOUNTERvalues c: CDENCOUNTERvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
