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
// Generated on: 2019.06.14 at 03:48:37 PM CEST
//


package org.taktik.icure.services.external.rest.v1.dto.be.ehealth.kmehr.v20131001.be.fgov.ehealth.standards.kmehr.cd.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CD-ITEMschemes.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-ITEMschemes">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="CD-ITEM"/>
 *     &lt;enumeration value="CD-ITEM-MAA"/>
 *     &lt;enumeration value="CD-ITEM-CARENET"/>
 *     &lt;enumeration value="CD-LAB"/>
 *     &lt;enumeration value="CD-TECHNICAL"/>
 *     &lt;enumeration value="CD-CONTACT-PERSON"/>
 *     &lt;enumeration value="ICD"/>
 *     &lt;enumeration value="ICPC"/>
 *     &lt;enumeration value="LOCAL"/>
 *     &lt;enumeration value="CD-VACCINE"/>
 *     &lt;enumeration value="CD-ECG"/>
 *     &lt;enumeration value="CD-ECARE-CLINICAL"/>
 *     &lt;enumeration value="CD-ECARE-LAB"/>
 *     &lt;enumeration value="CD-ECARE-HAQ"/>
 *     &lt;enumeration value="CD-ITEM-EBIRTH"/>
 *     &lt;enumeration value="CD-PARAMETER"/>
 *     &lt;enumeration value="CD-ITEM-BVT"/>
 *     &lt;enumeration value="CD-BVT-AVAILABLEMATERIALS"/>
 *     &lt;enumeration value="CD-BVT-CONSERVATIONDELAY"/>
 *     &lt;enumeration value="CD-BVT-CONSERVATIONMODE"/>
 *     &lt;enumeration value="CD-BVT-SAMPLETYPE"/>
 *     &lt;enumeration value="CD-BCR-DIFFERENTATIONDEGREE"/>
 *     &lt;enumeration value="CD-BVT-LATERALITY"/>
 *     &lt;enumeration value="CD-BVT-PATIENTOPPOSITION"/>
 *     &lt;enumeration value="CD-BVT-STATUS"/>
 *     &lt;enumeration value="CD-ITEM-REG"/>
 *     &lt;enumeration value="CD-ITEM-MYCARENET"/>
 *     &lt;enumeration value="CD-DEFIB-DIAGNOSIS"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 *
 */
@XmlType(name = "CD-ITEMschemes")
@XmlEnum
public enum CDITEMschemes {

    @XmlEnumValue("CD-ITEM")
    CD_ITEM("CD-ITEM"),
    @XmlEnumValue("CD-ITEM-MAA")
    CD_ITEM_MAA("CD-ITEM-MAA"),
    @XmlEnumValue("CD-ITEM-CARENET")
    CD_ITEM_CARENET("CD-ITEM-CARENET"),
    @XmlEnumValue("CD-LAB")
    CD_LAB("CD-LAB"),
    @XmlEnumValue("CD-TECHNICAL")
    CD_TECHNICAL("CD-TECHNICAL"),
    @XmlEnumValue("CD-CONTACT-PERSON")
    CD_CONTACT_PERSON("CD-CONTACT-PERSON"),
    ICD("ICD"),
    ICPC("ICPC"),
    LOCAL("LOCAL"),
    @XmlEnumValue("CD-VACCINE")
    CD_VACCINE("CD-VACCINE"),
    @XmlEnumValue("CD-ECG")
    CD_ECG("CD-ECG"),
    @XmlEnumValue("CD-ECARE-CLINICAL")
    CD_ECARE_CLINICAL("CD-ECARE-CLINICAL"),
    @XmlEnumValue("CD-ECARE-LAB")
    CD_ECARE_LAB("CD-ECARE-LAB"),
    @XmlEnumValue("CD-ECARE-HAQ")
    CD_ECARE_HAQ("CD-ECARE-HAQ"),
    @XmlEnumValue("CD-ITEM-EBIRTH")
    CD_ITEM_EBIRTH("CD-ITEM-EBIRTH"),
    @XmlEnumValue("CD-PARAMETER")
    CD_PARAMETER("CD-PARAMETER"),
    @XmlEnumValue("CD-ITEM-BVT")
    CD_ITEM_BVT("CD-ITEM-BVT"),
    @XmlEnumValue("CD-BVT-AVAILABLEMATERIALS")
    CD_BVT_AVAILABLEMATERIALS("CD-BVT-AVAILABLEMATERIALS"),
    @XmlEnumValue("CD-BVT-CONSERVATIONDELAY")
    CD_BVT_CONSERVATIONDELAY("CD-BVT-CONSERVATIONDELAY"),
    @XmlEnumValue("CD-BVT-CONSERVATIONMODE")
    CD_BVT_CONSERVATIONMODE("CD-BVT-CONSERVATIONMODE"),
    @XmlEnumValue("CD-BVT-SAMPLETYPE")
    CD_BVT_SAMPLETYPE("CD-BVT-SAMPLETYPE"),
    @XmlEnumValue("CD-BCR-DIFFERENTATIONDEGREE")
    CD_BCR_DIFFERENTATIONDEGREE("CD-BCR-DIFFERENTATIONDEGREE"),
    @XmlEnumValue("CD-BVT-LATERALITY")
    CD_BVT_LATERALITY("CD-BVT-LATERALITY"),
    @XmlEnumValue("CD-BVT-PATIENTOPPOSITION")
    CD_BVT_PATIENTOPPOSITION("CD-BVT-PATIENTOPPOSITION"),
    @XmlEnumValue("CD-BVT-STATUS")
    CD_BVT_STATUS("CD-BVT-STATUS"),
    @XmlEnumValue("CD-ITEM-REG")
    CD_ITEM_REG("CD-ITEM-REG"),
    @XmlEnumValue("CD-ITEM-MYCARENET")
    CD_ITEM_MYCARENET("CD-ITEM-MYCARENET"),
    @XmlEnumValue("CD-DEFIB-DIAGNOSIS")
    CD_DEFIB_DIAGNOSIS("CD-DEFIB-DIAGNOSIS");
    private final String value;

    CDITEMschemes(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDITEMschemes fromValue(String v) {
        for (CDITEMschemes c: CDITEMschemes.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
