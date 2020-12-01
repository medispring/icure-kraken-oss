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
// Generated on: 2019.06.14 at 03:50:09 PM CEST
//


package org.taktik.icure.be.ehealth.dto.kmehr.v20170601.be.fgov.ehealth.standards.kmehr.cd.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CD-SUBSTANCEschemes.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-SUBSTANCEschemes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CD-INNCLUSTER"/>
 *     &lt;enumeration value="CD-ATC"/>
 *     &lt;enumeration value="CD-EAN"/>
 *     &lt;enumeration value="CD-SUBSTANCE-CNK"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "CD-SUBSTANCEschemes")
@XmlEnum
public enum CDSUBSTANCEschemes {

    @XmlEnumValue("CD-INNCLUSTER")
    CD_INNCLUSTER("CD-INNCLUSTER"),
    @XmlEnumValue("CD-ATC")
    CD_ATC("CD-ATC"),
    @XmlEnumValue("CD-EAN")
    CD_EAN("CD-EAN"),
    @XmlEnumValue("CD-SUBSTANCE-CNK")
    CD_SUBSTANCE_CNK("CD-SUBSTANCE-CNK");
    private final String value;

    CDSUBSTANCEschemes(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDSUBSTANCEschemes fromValue(String v) {
        for (CDSUBSTANCEschemes c: CDSUBSTANCEschemes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
