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
// Ce fichier a été généré par l'implémentation de référence JavaTM Architecture for XML Binding (JAXB), v2.2.8-b130911.1802 
// Voir <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Toute modification apportée à ce fichier sera perdue lors de la recompilation du schéma source. 
// Généré le : 2015.03.05 à 11:47:59 AM CET 
//


package org.taktik.icure.be.ehealth.dto.kmehr.v20110701.be.fgov.ehealth.standards.kmehr.cd.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour CD-SITEvalues.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-SITEvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="BE"/>
 *     &lt;enumeration value="blood"/>
 *     &lt;enumeration value="BN"/>
 *     &lt;enumeration value="BU"/>
 *     &lt;enumeration value="CT"/>
 *     &lt;enumeration value="LA"/>
 *     &lt;enumeration value="LAC"/>
 *     &lt;enumeration value="LACF"/>
 *     &lt;enumeration value="LD"/>
 *     &lt;enumeration value="LE"/>
 *     &lt;enumeration value="LEJ"/>
 *     &lt;enumeration value="LF"/>
 *     &lt;enumeration value="LG"/>
 *     &lt;enumeration value="LH"/>
 *     &lt;enumeration value="LIJ"/>
 *     &lt;enumeration value="LLAQ"/>
 *     &lt;enumeration value="LLFA"/>
 *     &lt;enumeration value="LMFA"/>
 *     &lt;enumeration value="LN"/>
 *     &lt;enumeration value="LPC"/>
 *     &lt;enumeration value="LSC"/>
 *     &lt;enumeration value="LT"/>
 *     &lt;enumeration value="LUA"/>
 *     &lt;enumeration value="LUAQ"/>
 *     &lt;enumeration value="LUFA"/>
 *     &lt;enumeration value="LVG"/>
 *     &lt;enumeration value="LVL"/>
 *     &lt;enumeration value="NB"/>
 *     &lt;enumeration value="OD"/>
 *     &lt;enumeration value="OS"/>
 *     &lt;enumeration value="OU"/>
 *     &lt;enumeration value="PA"/>
 *     &lt;enumeration value="PERIN"/>
 *     &lt;enumeration value="RA"/>
 *     &lt;enumeration value="RAC"/>
 *     &lt;enumeration value="RACF"/>
 *     &lt;enumeration value="RD"/>
 *     &lt;enumeration value="RE"/>
 *     &lt;enumeration value="REJ"/>
 *     &lt;enumeration value="RF"/>
 *     &lt;enumeration value="RG"/>
 *     &lt;enumeration value="RH"/>
 *     &lt;enumeration value="RIJ"/>
 *     &lt;enumeration value="RLAQ"/>
 *     &lt;enumeration value="RLFA"/>
 *     &lt;enumeration value="RMFA"/>
 *     &lt;enumeration value="RN"/>
 *     &lt;enumeration value="RPC"/>
 *     &lt;enumeration value="RSC"/>
 *     &lt;enumeration value="RT"/>
 *     &lt;enumeration value="RUA"/>
 *     &lt;enumeration value="RUAQ"/>
 *     &lt;enumeration value="RUFA"/>
 *     &lt;enumeration value="RVG"/>
 *     &lt;enumeration value="RVL"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CD-SITEvalues")
@XmlEnum
public enum CDSITEvalues {

    BE("BE"),
    @XmlEnumValue("blood")
    BLOOD("blood"),
    BN("BN"),
    BU("BU"),
    CT("CT"),
    LA("LA"),
    LAC("LAC"),
    LACF("LACF"),
    LD("LD"),
    LE("LE"),
    LEJ("LEJ"),
    LF("LF"),
    LG("LG"),
    LH("LH"),
    LIJ("LIJ"),
    LLAQ("LLAQ"),
    LLFA("LLFA"),
    LMFA("LMFA"),
    LN("LN"),
    LPC("LPC"),
    LSC("LSC"),
    LT("LT"),
    LUA("LUA"),
    LUAQ("LUAQ"),
    LUFA("LUFA"),
    LVG("LVG"),
    LVL("LVL"),
    NB("NB"),
    OD("OD"),
    OS("OS"),
    OU("OU"),
    PA("PA"),
    PERIN("PERIN"),
    RA("RA"),
    RAC("RAC"),
    RACF("RACF"),
    RD("RD"),
    RE("RE"),
    REJ("REJ"),
    RF("RF"),
    RG("RG"),
    RH("RH"),
    RIJ("RIJ"),
    RLAQ("RLAQ"),
    RLFA("RLFA"),
    RMFA("RMFA"),
    RN("RN"),
    RPC("RPC"),
    RSC("RSC"),
    RT("RT"),
    RUA("RUA"),
    RUAQ("RUAQ"),
    RUFA("RUFA"),
    RVG("RVG"),
    RVL("RVL");
    private final String value;

    CDSITEvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDSITEvalues fromValue(String v) {
        for (CDSITEvalues c: CDSITEvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
