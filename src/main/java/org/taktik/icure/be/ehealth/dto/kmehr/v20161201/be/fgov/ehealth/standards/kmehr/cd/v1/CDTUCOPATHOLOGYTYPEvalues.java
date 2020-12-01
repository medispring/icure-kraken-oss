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
// Generated on: 2019.06.14 at 03:49:53 PM CEST
//


package org.taktik.icure.be.ehealth.dto.kmehr.v20161201.be.fgov.ehealth.standards.kmehr.cd.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CD-TUCO-PATHOLOGYTYPEvalues.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-TUCO-PATHOLOGYTYPEvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="diabetedietarycontrol"/>
 *     &lt;enumeration value="diabeteoralmedication"/>
 *     &lt;enumeration value="diabeteinsulin"/>
 *     &lt;enumeration value="diabetenewlydiagnosed"/>
 *     &lt;enumeration value="diabete"/>
 *     &lt;enumeration value="renalfailurelessthan30ml"/>
 *     &lt;enumeration value="instentrestenosis"/>
 *     &lt;enumeration value="cardioshockatstartpci"/>
 *     &lt;enumeration value="stroke"/>
 *     &lt;enumeration value="peripheralvasculardisease"/>
 *     &lt;enumeration value="stentthrombosis"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "CD-TUCO-PATHOLOGYTYPEvalues")
@XmlEnum
public enum CDTUCOPATHOLOGYTYPEvalues {

    @XmlEnumValue("diabetedietarycontrol")
    DIABETEDIETARYCONTROL("diabetedietarycontrol"),
    @XmlEnumValue("diabeteoralmedication")
    DIABETEORALMEDICATION("diabeteoralmedication"),
    @XmlEnumValue("diabeteinsulin")
    DIABETEINSULIN("diabeteinsulin"),
    @XmlEnumValue("diabetenewlydiagnosed")
    DIABETENEWLYDIAGNOSED("diabetenewlydiagnosed"),
    @XmlEnumValue("diabete")
    DIABETE("diabete"),
    @XmlEnumValue("renalfailurelessthan30ml")
    RENALFAILURELESSTHAN_30_ML("renalfailurelessthan30ml"),
    @XmlEnumValue("instentrestenosis")
    INSTENTRESTENOSIS("instentrestenosis"),
    @XmlEnumValue("cardioshockatstartpci")
    CARDIOSHOCKATSTARTPCI("cardioshockatstartpci"),
    @XmlEnumValue("stroke")
    STROKE("stroke"),
    @XmlEnumValue("peripheralvasculardisease")
    PERIPHERALVASCULARDISEASE("peripheralvasculardisease"),
    @XmlEnumValue("stentthrombosis")
    STENTTHROMBOSIS("stentthrombosis");
    private final String value;

    CDTUCOPATHOLOGYTYPEvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDTUCOPATHOLOGYTYPEvalues fromValue(String v) {
        for (CDTUCOPATHOLOGYTYPEvalues c: CDTUCOPATHOLOGYTYPEvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
