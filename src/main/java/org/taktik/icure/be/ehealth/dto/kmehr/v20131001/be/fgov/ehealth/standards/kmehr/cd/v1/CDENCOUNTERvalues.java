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
// Generated on: 2019.06.14 at 03:48:36 PM CEST
//


package org.taktik.icure.be.ehealth.dto.kmehr.v20131001.be.fgov.ehealth.standards.kmehr.cd.v1;

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
