//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.06.14 at 03:49:43 PM CEST 
//


package org.taktik.icure.services.external.rest.v1.dto.be.ehealth.kmehr.v20190301.be.fgov.ehealth.standards.kmehr.cd.v1;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for CD-ITEM-CARENETvalues.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="CD-ITEM-CARENETvalues">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="accidenttype"/>
 *     &lt;enumeration value="advisingphysician"/>
 *     &lt;enumeration value="agreement"/>
 *     &lt;enumeration value="authorisedextensionenddatetime"/>
 *     &lt;enumeration value="begindatetime"/>
 *     &lt;enumeration value="billingdestinationnumber"/>
 *     &lt;enumeration value="insurancydetails"/>
 *     &lt;enumeration value="insurancystatus"/>
 *     &lt;enumeration value="messagenumber"/>
 *     &lt;enumeration value="messagetype"/>
 *     &lt;enumeration value="missingdocument"/>
 *     &lt;enumeration value="mutationbegindatetime"/>
 *     &lt;enumeration value="mutationdestination"/>
 *     &lt;enumeration value="nationalinsurance"/>
 *     &lt;enumeration value="otheradmission"/>
 *     &lt;enumeration value="protectionmeasure"/>
 *     &lt;enumeration value="refusalreason"/>
 *     &lt;enumeration value="requestedextensiondatetime"/>
 *     &lt;enumeration value="requestedextensionenddatetime"/>
 *     &lt;enumeration value="siscardadjustment"/>
 *     &lt;enumeration value="siscarderror"/>
 *     &lt;enumeration value="socialcategory"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "CD-ITEM-CARENETvalues")
@XmlEnum
public enum CDITEMCARENETvalues {

    @XmlEnumValue("accidenttype")
    ACCIDENTTYPE("accidenttype"),
    @XmlEnumValue("advisingphysician")
    ADVISINGPHYSICIAN("advisingphysician"),
    @XmlEnumValue("agreement")
    AGREEMENT("agreement"),
    @XmlEnumValue("authorisedextensionenddatetime")
    AUTHORISEDEXTENSIONENDDATETIME("authorisedextensionenddatetime"),
    @XmlEnumValue("begindatetime")
    BEGINDATETIME("begindatetime"),
    @XmlEnumValue("billingdestinationnumber")
    BILLINGDESTINATIONNUMBER("billingdestinationnumber"),
    @XmlEnumValue("insurancydetails")
    INSURANCYDETAILS("insurancydetails"),
    @XmlEnumValue("insurancystatus")
    INSURANCYSTATUS("insurancystatus"),
    @XmlEnumValue("messagenumber")
    MESSAGENUMBER("messagenumber"),
    @XmlEnumValue("messagetype")
    MESSAGETYPE("messagetype"),
    @XmlEnumValue("missingdocument")
    MISSINGDOCUMENT("missingdocument"),
    @XmlEnumValue("mutationbegindatetime")
    MUTATIONBEGINDATETIME("mutationbegindatetime"),
    @XmlEnumValue("mutationdestination")
    MUTATIONDESTINATION("mutationdestination"),
    @XmlEnumValue("nationalinsurance")
    NATIONALINSURANCE("nationalinsurance"),
    @XmlEnumValue("otheradmission")
    OTHERADMISSION("otheradmission"),
    @XmlEnumValue("protectionmeasure")
    PROTECTIONMEASURE("protectionmeasure"),
    @XmlEnumValue("refusalreason")
    REFUSALREASON("refusalreason"),
    @XmlEnumValue("requestedextensiondatetime")
    REQUESTEDEXTENSIONDATETIME("requestedextensiondatetime"),
    @XmlEnumValue("requestedextensionenddatetime")
    REQUESTEDEXTENSIONENDDATETIME("requestedextensionenddatetime"),
    @XmlEnumValue("siscardadjustment")
    SISCARDADJUSTMENT("siscardadjustment"),
    @XmlEnumValue("siscarderror")
    SISCARDERROR("siscarderror"),
    @XmlEnumValue("socialcategory")
    SOCIALCATEGORY("socialcategory");
    private final String value;

    CDITEMCARENETvalues(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static CDITEMCARENETvalues fromValue(String v) {
        for (CDITEMCARENETvalues c: CDITEMCARENETvalues.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
