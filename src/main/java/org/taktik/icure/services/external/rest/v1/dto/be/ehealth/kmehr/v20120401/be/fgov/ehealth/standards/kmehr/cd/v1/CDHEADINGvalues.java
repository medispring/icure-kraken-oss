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
// Generated on: 2019.06.14 at 03:48:29 PM CEST
//


package org.taktik.icure.services.external.rest.v1.dto.be.ehealth.kmehr.v20120401.be.fgov.ehealth.standards.kmehr.cd.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CD-HEADINGvalues.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-HEADINGvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="assessment"/>
 *     &lt;enumeration value="clinical"/>
 *     &lt;enumeration value="clinicalplan"/>
 *     &lt;enumeration value="subjective"/>
 *     &lt;enumeration value="technical"/>
 *     &lt;enumeration value="technicalplan"/>
 *     &lt;enumeration value="treatment"/>
 *     &lt;enumeration value="userdefined"/>
 *     &lt;enumeration value="history"/>
 *     &lt;enumeration value="familyhistory"/>
 *     &lt;enumeration value="prescription"/>
 *     &lt;enumeration value="medication"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "CD-HEADINGvalues")
@XmlEnum
public enum CDHEADINGvalues {

    @XmlEnumValue("assessment")
    ASSESSMENT("assessment"),
    @XmlEnumValue("clinical")
    CLINICAL("clinical"),
    @XmlEnumValue("clinicalplan")
    CLINICALPLAN("clinicalplan"),
    @XmlEnumValue("subjective")
    SUBJECTIVE("subjective"),
    @XmlEnumValue("technical")
    TECHNICAL("technical"),
    @XmlEnumValue("technicalplan")
    TECHNICALPLAN("technicalplan"),
    @XmlEnumValue("treatment")
    TREATMENT("treatment"),
    @XmlEnumValue("userdefined")
    USERDEFINED("userdefined"),
    @XmlEnumValue("history")
    HISTORY("history"),
    @XmlEnumValue("familyhistory")
    FAMILYHISTORY("familyhistory"),
    @XmlEnumValue("prescription")
    PRESCRIPTION("prescription"),
    @XmlEnumValue("medication")
    MEDICATION("medication");
    private final String value;

    CDHEADINGvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDHEADINGvalues fromValue(String v) {
        for (CDHEADINGvalues c: CDHEADINGvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
