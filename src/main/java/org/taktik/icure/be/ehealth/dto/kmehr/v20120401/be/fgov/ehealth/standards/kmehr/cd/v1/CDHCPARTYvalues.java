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
// Generated on: 2019.06.14 at 03:48:28 PM CEST
//


package org.taktik.icure.be.ehealth.dto.kmehr.v20120401.be.fgov.ehealth.standards.kmehr.cd.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CD-HCPARTYvalues.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-HCPARTYvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="deptanatomopathology"/>
 *     &lt;enumeration value="deptanesthesiology"/>
 *     &lt;enumeration value="deptbacteriology"/>
 *     &lt;enumeration value="deptcardiology"/>
 *     &lt;enumeration value="deptdermatology"/>
 *     &lt;enumeration value="deptdietetics"/>
 *     &lt;enumeration value="deptemergency"/>
 *     &lt;enumeration value="deptgastroenterology"/>
 *     &lt;enumeration value="deptgeneralpractice"/>
 *     &lt;enumeration value="deptgenetics"/>
 *     &lt;enumeration value="deptgeriatry"/>
 *     &lt;enumeration value="deptgynecology"/>
 *     &lt;enumeration value="depthematology"/>
 *     &lt;enumeration value="deptintensivecare"/>
 *     &lt;enumeration value="deptkinesitherapy"/>
 *     &lt;enumeration value="deptlaboratory"/>
 *     &lt;enumeration value="deptmedicine"/>
 *     &lt;enumeration value="deptmolecularbiology"/>
 *     &lt;enumeration value="deptneonatalogy"/>
 *     &lt;enumeration value="deptnephrology"/>
 *     &lt;enumeration value="deptneurology"/>
 *     &lt;enumeration value="deptnte"/>
 *     &lt;enumeration value="deptnuclear"/>
 *     &lt;enumeration value="deptoncology"/>
 *     &lt;enumeration value="deptophtalmology"/>
 *     &lt;enumeration value="deptpediatry"/>
 *     &lt;enumeration value="deptpharmacy"/>
 *     &lt;enumeration value="deptphysiotherapy"/>
 *     &lt;enumeration value="deptpneumology"/>
 *     &lt;enumeration value="deptpsychiatry"/>
 *     &lt;enumeration value="deptradiology"/>
 *     &lt;enumeration value="deptradiotherapy"/>
 *     &lt;enumeration value="deptrhumatology"/>
 *     &lt;enumeration value="deptstomatology"/>
 *     &lt;enumeration value="deptsurgery"/>
 *     &lt;enumeration value="depttoxicology"/>
 *     &lt;enumeration value="depturology"/>
 *     &lt;enumeration value="orghospital"/>
 *     &lt;enumeration value="orginsurance"/>
 *     &lt;enumeration value="orglaboratory"/>
 *     &lt;enumeration value="orgpublichealth"/>
 *     &lt;enumeration value="persbiologist"/>
 *     &lt;enumeration value="persdentist"/>
 *     &lt;enumeration value="persnurse"/>
 *     &lt;enumeration value="persparamedical"/>
 *     &lt;enumeration value="perspharmacist"/>
 *     &lt;enumeration value="persphysician"/>
 *     &lt;enumeration value="perssocialworker"/>
 *     &lt;enumeration value="perstechnician"/>
 *     &lt;enumeration value="persadministrative"/>
 *     &lt;enumeration value="persmidwife"/>
 *     &lt;enumeration value="ecaresafe"/>
 *     &lt;enumeration value="application"/>
 *     &lt;enumeration value="hub"/>
 *     &lt;enumeration value="deptorthopedy"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@XmlType(name = "CD-HCPARTYvalues")
@XmlEnum
public enum CDHCPARTYvalues {

    @XmlEnumValue("deptanatomopathology")
    DEPTANATOMOPATHOLOGY("deptanatomopathology"),
    @XmlEnumValue("deptanesthesiology")
    DEPTANESTHESIOLOGY("deptanesthesiology"),
    @XmlEnumValue("deptbacteriology")
    DEPTBACTERIOLOGY("deptbacteriology"),
    @XmlEnumValue("deptcardiology")
    DEPTCARDIOLOGY("deptcardiology"),
    @XmlEnumValue("deptdermatology")
    DEPTDERMATOLOGY("deptdermatology"),
    @XmlEnumValue("deptdietetics")
    DEPTDIETETICS("deptdietetics"),
    @XmlEnumValue("deptemergency")
    DEPTEMERGENCY("deptemergency"),
    @XmlEnumValue("deptgastroenterology")
    DEPTGASTROENTEROLOGY("deptgastroenterology"),
    @XmlEnumValue("deptgeneralpractice")
    DEPTGENERALPRACTICE("deptgeneralpractice"),
    @XmlEnumValue("deptgenetics")
    DEPTGENETICS("deptgenetics"),
    @XmlEnumValue("deptgeriatry")
    DEPTGERIATRY("deptgeriatry"),
    @XmlEnumValue("deptgynecology")
    DEPTGYNECOLOGY("deptgynecology"),
    @XmlEnumValue("depthematology")
    DEPTHEMATOLOGY("depthematology"),
    @XmlEnumValue("deptintensivecare")
    DEPTINTENSIVECARE("deptintensivecare"),
    @XmlEnumValue("deptkinesitherapy")
    DEPTKINESITHERAPY("deptkinesitherapy"),
    @XmlEnumValue("deptlaboratory")
    DEPTLABORATORY("deptlaboratory"),
    @XmlEnumValue("deptmedicine")
    DEPTMEDICINE("deptmedicine"),
    @XmlEnumValue("deptmolecularbiology")
    DEPTMOLECULARBIOLOGY("deptmolecularbiology"),
    @XmlEnumValue("deptneonatalogy")
    DEPTNEONATALOGY("deptneonatalogy"),
    @XmlEnumValue("deptnephrology")
    DEPTNEPHROLOGY("deptnephrology"),
    @XmlEnumValue("deptneurology")
    DEPTNEUROLOGY("deptneurology"),
    @XmlEnumValue("deptnte")
    DEPTNTE("deptnte"),
    @XmlEnumValue("deptnuclear")
    DEPTNUCLEAR("deptnuclear"),
    @XmlEnumValue("deptoncology")
    DEPTONCOLOGY("deptoncology"),
    @XmlEnumValue("deptophtalmology")
    DEPTOPHTALMOLOGY("deptophtalmology"),
    @XmlEnumValue("deptpediatry")
    DEPTPEDIATRY("deptpediatry"),
    @XmlEnumValue("deptpharmacy")
    DEPTPHARMACY("deptpharmacy"),
    @XmlEnumValue("deptphysiotherapy")
    DEPTPHYSIOTHERAPY("deptphysiotherapy"),
    @XmlEnumValue("deptpneumology")
    DEPTPNEUMOLOGY("deptpneumology"),
    @XmlEnumValue("deptpsychiatry")
    DEPTPSYCHIATRY("deptpsychiatry"),
    @XmlEnumValue("deptradiology")
    DEPTRADIOLOGY("deptradiology"),
    @XmlEnumValue("deptradiotherapy")
    DEPTRADIOTHERAPY("deptradiotherapy"),
    @XmlEnumValue("deptrhumatology")
    DEPTRHUMATOLOGY("deptrhumatology"),
    @XmlEnumValue("deptstomatology")
    DEPTSTOMATOLOGY("deptstomatology"),
    @XmlEnumValue("deptsurgery")
    DEPTSURGERY("deptsurgery"),
    @XmlEnumValue("depttoxicology")
    DEPTTOXICOLOGY("depttoxicology"),
    @XmlEnumValue("depturology")
    DEPTUROLOGY("depturology"),
    @XmlEnumValue("orghospital")
    ORGHOSPITAL("orghospital"),
    @XmlEnumValue("orginsurance")
    ORGINSURANCE("orginsurance"),
    @XmlEnumValue("orglaboratory")
    ORGLABORATORY("orglaboratory"),
    @XmlEnumValue("orgpublichealth")
    ORGPUBLICHEALTH("orgpublichealth"),
    @XmlEnumValue("persbiologist")
    PERSBIOLOGIST("persbiologist"),
    @XmlEnumValue("persdentist")
    PERSDENTIST("persdentist"),
    @XmlEnumValue("persnurse")
    PERSNURSE("persnurse"),
    @XmlEnumValue("persparamedical")
    PERSPARAMEDICAL("persparamedical"),
    @XmlEnumValue("perspharmacist")
    PERSPHARMACIST("perspharmacist"),
    @XmlEnumValue("persphysician")
    PERSPHYSICIAN("persphysician"),
    @XmlEnumValue("perssocialworker")
    PERSSOCIALWORKER("perssocialworker"),
    @XmlEnumValue("perstechnician")
    PERSTECHNICIAN("perstechnician"),
    @XmlEnumValue("persadministrative")
    PERSADMINISTRATIVE("persadministrative"),
    @XmlEnumValue("persmidwife")
    PERSMIDWIFE("persmidwife"),
    @XmlEnumValue("ecaresafe")
    ECARESAFE("ecaresafe"),
    @XmlEnumValue("application")
    APPLICATION("application"),
    @XmlEnumValue("hub")
    HUB("hub"),
    @XmlEnumValue("deptorthopedy")
    DEPTORTHOPEDY("deptorthopedy");
    private final String value;

    CDHCPARTYvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDHCPARTYvalues fromValue(String v) {
        for (CDHCPARTYvalues c : CDHCPARTYvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
