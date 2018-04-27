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
// Généré le : 2015.03.05 à 11:47:54 AM CET 
//


package org.taktik.icure.services.external.rest.v1.dto.be.ehealth.kmehr.v20100601.be.fgov.ehealth.standards.kmehr.cd.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java pour CD-ITEMvalues.
 * 
 * <p>Le fragment de schéma suivant indique le contenu attendu figurant dans cette classe.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-ITEMvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="admissiontype"/>
 *     &lt;enumeration value="adr"/>
 *     &lt;enumeration value="allergy"/>
 *     &lt;enumeration value="autonomy"/>
 *     &lt;enumeration value="bloodtransfusionrefusal"/>
 *     &lt;enumeration value="clinical"/>
 *     &lt;enumeration value="complaint"/>
 *     &lt;enumeration value="conclusion"/>
 *     &lt;enumeration value="contactperson"/>
 *     &lt;enumeration value="dischargedatetime"/>
 *     &lt;enumeration value="dischargedestination"/>
 *     &lt;enumeration value="dischargetype"/>
 *     &lt;enumeration value="encounterdatetime"/>
 *     &lt;enumeration value="transferdatetime"/>
 *     &lt;enumeration value="encounterlocation"/>
 *     &lt;enumeration value="encounterlegalservice"/>
 *     &lt;enumeration value="encounterresponsible"/>
 *     &lt;enumeration value="encountertype"/>
 *     &lt;enumeration value="encountersafetyissue"/>
 *     &lt;enumeration value="evolution"/>
 *     &lt;enumeration value="expirationdatetime"/>
 *     &lt;enumeration value="gmdmanager"/>
 *     &lt;enumeration value="habit"/>
 *     &lt;enumeration value="hcpartyavailability"/>
 *     &lt;enumeration value="healthcareelement"/>
 *     &lt;enumeration value="medication"/>
 *     &lt;enumeration value="incapacity"/>
 *     &lt;enumeration value="ntbr"/>
 *     &lt;enumeration value="complementaryproduct"/>
 *     &lt;enumeration value="requestnumber"/>
 *     &lt;enumeration value="requestdatetime"/>
 *     &lt;enumeration value="requestor"/>
 *     &lt;enumeration value="lab"/>
 *     &lt;enumeration value="referrer"/>
 *     &lt;enumeration value="reimbursementcertificate"/>
 *     &lt;enumeration value="requesteddecisionsharing"/>
 *     &lt;enumeration value="requesteddischargedestination"/>
 *     &lt;enumeration value="requestedencountertype"/>
 *     &lt;enumeration value="requestedrecipient"/>
 *     &lt;enumeration value="risk"/>
 *     &lt;enumeration value="socialrisk"/>
 *     &lt;enumeration value="specimendatetime"/>
 *     &lt;enumeration value="technical"/>
 *     &lt;enumeration value="transactionreason"/>
 *     &lt;enumeration value="transcriptionist"/>
 *     &lt;enumeration value="treatment"/>
 *     &lt;enumeration value="vaccine"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CD-ITEMvalues")
@XmlEnum
public enum CDITEMvalues {

    @XmlEnumValue("admissiontype")
    ADMISSIONTYPE("admissiontype"),
    @XmlEnumValue("adr")
    ADR("adr"),
    @XmlEnumValue("allergy")
    ALLERGY("allergy"),
    @XmlEnumValue("autonomy")
    AUTONOMY("autonomy"),
    @XmlEnumValue("bloodtransfusionrefusal")
    BLOODTRANSFUSIONREFUSAL("bloodtransfusionrefusal"),
    @XmlEnumValue("clinical")
    CLINICAL("clinical"),
    @XmlEnumValue("complaint")
    COMPLAINT("complaint"),
    @XmlEnumValue("conclusion")
    CONCLUSION("conclusion"),
    @XmlEnumValue("contactperson")
    CONTACTPERSON("contactperson"),
    @XmlEnumValue("dischargedatetime")
    DISCHARGEDATETIME("dischargedatetime"),
    @XmlEnumValue("dischargedestination")
    DISCHARGEDESTINATION("dischargedestination"),
    @XmlEnumValue("dischargetype")
    DISCHARGETYPE("dischargetype"),
    @XmlEnumValue("encounterdatetime")
    ENCOUNTERDATETIME("encounterdatetime"),
    @XmlEnumValue("transferdatetime")
    TRANSFERDATETIME("transferdatetime"),
    @XmlEnumValue("encounterlocation")
    ENCOUNTERLOCATION("encounterlocation"),
    @XmlEnumValue("encounterlegalservice")
    ENCOUNTERLEGALSERVICE("encounterlegalservice"),
    @XmlEnumValue("encounterresponsible")
    ENCOUNTERRESPONSIBLE("encounterresponsible"),
    @XmlEnumValue("encountertype")
    ENCOUNTERTYPE("encountertype"),
    @XmlEnumValue("encountersafetyissue")
    ENCOUNTERSAFETYISSUE("encountersafetyissue"),
    @XmlEnumValue("evolution")
    EVOLUTION("evolution"),
    @XmlEnumValue("expirationdatetime")
    EXPIRATIONDATETIME("expirationdatetime"),
    @XmlEnumValue("gmdmanager")
    GMDMANAGER("gmdmanager"),
    @XmlEnumValue("habit")
    HABIT("habit"),
    @XmlEnumValue("hcpartyavailability")
    HCPARTYAVAILABILITY("hcpartyavailability"),
    @XmlEnumValue("healthcareelement")
    HEALTHCAREELEMENT("healthcareelement"),
    @XmlEnumValue("medication")
    MEDICATION("medication"),
    @XmlEnumValue("incapacity")
    INCAPACITY("incapacity"),
    @XmlEnumValue("ntbr")
    NTBR("ntbr"),
    @XmlEnumValue("complementaryproduct")
    COMPLEMENTARYPRODUCT("complementaryproduct"),
    @XmlEnumValue("requestnumber")
    REQUESTNUMBER("requestnumber"),
    @XmlEnumValue("requestdatetime")
    REQUESTDATETIME("requestdatetime"),
    @XmlEnumValue("requestor")
    REQUESTOR("requestor"),
    @XmlEnumValue("lab")
    LAB("lab"),
    @XmlEnumValue("referrer")
    REFERRER("referrer"),
    @XmlEnumValue("reimbursementcertificate")
    REIMBURSEMENTCERTIFICATE("reimbursementcertificate"),
    @XmlEnumValue("requesteddecisionsharing")
    REQUESTEDDECISIONSHARING("requesteddecisionsharing"),
    @XmlEnumValue("requesteddischargedestination")
    REQUESTEDDISCHARGEDESTINATION("requesteddischargedestination"),
    @XmlEnumValue("requestedencountertype")
    REQUESTEDENCOUNTERTYPE("requestedencountertype"),
    @XmlEnumValue("requestedrecipient")
    REQUESTEDRECIPIENT("requestedrecipient"),
    @XmlEnumValue("risk")
    RISK("risk"),
    @XmlEnumValue("socialrisk")
    SOCIALRISK("socialrisk"),
    @XmlEnumValue("specimendatetime")
    SPECIMENDATETIME("specimendatetime"),
    @XmlEnumValue("technical")
    TECHNICAL("technical"),
    @XmlEnumValue("transactionreason")
    TRANSACTIONREASON("transactionreason"),
    @XmlEnumValue("transcriptionist")
    TRANSCRIPTIONIST("transcriptionist"),
    @XmlEnumValue("treatment")
    TREATMENT("treatment"),
    @XmlEnumValue("vaccine")
    VACCINE("vaccine");
    private final String value;

    CDITEMvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDITEMvalues fromValue(String v) {
        for (CDITEMvalues c: CDITEMvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
