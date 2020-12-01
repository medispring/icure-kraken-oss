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
 * <p>Java class for CD-HEADING-REGvalues.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-HEADING-REGvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="coronaryanatomy"/>
 *     &lt;enumeration value="chapter4"/>
 *     &lt;enumeration value="non-biologic"/>
 *     &lt;enumeration value="bmi"/>
 *     &lt;enumeration value="primarykneeprocedure"/>
 *     &lt;enumeration value="approachtechnic"/>
 *     &lt;enumeration value="instrumentation"/>
 *     &lt;enumeration value="orthopedicanatomy"/>
 *     &lt;enumeration value="interface"/>
 *     &lt;enumeration value="revisionplan"/>
 *     &lt;enumeration value="material"/>
 *     &lt;enumeration value="notified-material"/>
 *     &lt;enumeration value="not-notified-material"/>
 *     &lt;enumeration value="not-notified-ortho-device"/>
 *     &lt;enumeration value="criteria"/>
 *     &lt;enumeration value="comorbidities"/>
 *     &lt;enumeration value="comorbiditiesinformation"/>
 *     &lt;enumeration value="results"/>
 *     &lt;enumeration value="resynchronisationinfo"/>
 *     &lt;enumeration value="crtp"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "CD-HEADING-REGvalues")
@XmlEnum
public enum CDHEADINGREGvalues {

    @XmlEnumValue("coronaryanatomy")
    CORONARYANATOMY("coronaryanatomy"),
    @XmlEnumValue("chapter4")
    CHAPTER_4("chapter4"),
    @XmlEnumValue("non-biologic")
    NON_BIOLOGIC("non-biologic"),
    @XmlEnumValue("bmi")
    BMI("bmi"),
    @XmlEnumValue("primarykneeprocedure")
    PRIMARYKNEEPROCEDURE("primarykneeprocedure"),
    @XmlEnumValue("approachtechnic")
    APPROACHTECHNIC("approachtechnic"),
    @XmlEnumValue("instrumentation")
    INSTRUMENTATION("instrumentation"),
    @XmlEnumValue("orthopedicanatomy")
    ORTHOPEDICANATOMY("orthopedicanatomy"),
    @XmlEnumValue("interface")
    INTERFACE("interface"),
    @XmlEnumValue("revisionplan")
    REVISIONPLAN("revisionplan"),
    @XmlEnumValue("material")
    MATERIAL("material"),
    @XmlEnumValue("notified-material")
    NOTIFIED_MATERIAL("notified-material"),
    @XmlEnumValue("not-notified-material")
    NOT_NOTIFIED_MATERIAL("not-notified-material"),
    @XmlEnumValue("not-notified-ortho-device")
    NOT_NOTIFIED_ORTHO_DEVICE("not-notified-ortho-device"),
    @XmlEnumValue("criteria")
    CRITERIA("criteria"),
    @XmlEnumValue("comorbidities")
    COMORBIDITIES("comorbidities"),
    @XmlEnumValue("comorbiditiesinformation")
    COMORBIDITIESINFORMATION("comorbiditiesinformation"),
    @XmlEnumValue("results")
    RESULTS("results"),
    @XmlEnumValue("resynchronisationinfo")
    RESYNCHRONISATIONINFO("resynchronisationinfo"),
    @XmlEnumValue("crtp")
    CRTP("crtp");
    private final String value;

    CDHEADINGREGvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDHEADINGREGvalues fromValue(String v) {
        for (CDHEADINGREGvalues c: CDHEADINGREGvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
