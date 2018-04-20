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
 * <p>Java class for CD-ORTHO-GRAFTvalues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-ORTHO-GRAFTvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="allograft"/>
 *     &lt;enumeration value="homograft"/>
 *     &lt;enumeration value="autograft"/>
 *     &lt;enumeration value="alloandautograft"/>
 *     &lt;enumeration value="none"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CD-ORTHO-GRAFTvalues")
@XmlEnum
public enum CDORTHOGRAFTvalues {

    @XmlEnumValue("allograft")
    ALLOGRAFT("allograft"),
    @XmlEnumValue("homograft")
    HOMOGRAFT("homograft"),
    @XmlEnumValue("autograft")
    AUTOGRAFT("autograft"),
    @XmlEnumValue("alloandautograft")
    ALLOANDAUTOGRAFT("alloandautograft"),
    @XmlEnumValue("none")
    NONE("none");
    private final String value;

    CDORTHOGRAFTvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDORTHOGRAFTvalues fromValue(String v) {
        for (CDORTHOGRAFTvalues c: CDORTHOGRAFTvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
