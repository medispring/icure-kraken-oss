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
// Generated on: 2019.06.14 at 03:49:25 PM CEST
//


package org.taktik.icure.be.ehealth.dto.kmehr.v20150901.be.fgov.ehealth.standards.kmehr.cd.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CD-ITEM-BVTvalues.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-ITEM-BVTvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="referenceid"/>
 *     &lt;enumeration value="patientopposition"/>
 *     &lt;enumeration value="sample"/>
 *     &lt;enumeration value="biopsynumber"/>
 *     &lt;enumeration value="technicalremarks"/>
 *     &lt;enumeration value="lab"/>
 *     &lt;enumeration value="error"/>
 *     &lt;enumeration value="status"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "CD-ITEM-BVTvalues")
@XmlEnum
public enum CDITEMBVTvalues {

    @XmlEnumValue("referenceid")
    REFERENCEID("referenceid"),
    @XmlEnumValue("patientopposition")
    PATIENTOPPOSITION("patientopposition"),
    @XmlEnumValue("sample")
    SAMPLE("sample"),
    @XmlEnumValue("biopsynumber")
    BIOPSYNUMBER("biopsynumber"),
    @XmlEnumValue("technicalremarks")
    TECHNICALREMARKS("technicalremarks"),
    @XmlEnumValue("lab")
    LAB("lab"),
    @XmlEnumValue("error")
    ERROR("error"),
    @XmlEnumValue("status")
    STATUS("status");
    private final String value;

    CDITEMBVTvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDITEMBVTvalues fromValue(String v) {
        for (CDITEMBVTvalues c: CDITEMBVTvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
