//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2019.05.22 at 08:11:32 PM CEST
//


package org.taktik.icure.be.samv2.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import org.taktik.icure.be.ehealth.samws.v2.reimbursementlaw.submit.LegalReferenceKeyType;


/**
 * <p>Java class for LegalReferenceFullDataType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="LegalReferenceFullDataType">
 *   &lt;complexContent>
 *     &lt;extension base="{urn:be:fgov:ehealth:samws:v2:reimbursementlaw:submit}LegalReferenceKeyType">
 *       &lt;sequence>
 *         &lt;element name="Data" type="{urn:be:fgov:ehealth:samws:v2:export}LegalReferenceDataType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;group ref="{urn:be:fgov:ehealth:samws:v2:reimbursementlaw:submit}LegalReferenceReferences"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "LegalReferenceFullDataType", propOrder = {
    "datas",
    "legalReferenceTraces"
})
@XmlSeeAlso({
    RecursiveLegalReferenceFullDataType.class
})
public class LegalReferenceFullDataType
    extends LegalReferenceKeyType
    implements Serializable
{

    private final static long serialVersionUID = 2L;
    @XmlElement(name = "Data")
    protected List<LegalReferenceDataType> datas;
    @XmlElement(name = "LegalReferenceTrace", namespace = "urn:be:fgov:ehealth:samws:v2:reimbursementlaw:submit")
    protected List<LegalReferenceKeyType> legalReferenceTraces;

    /**
     * Gets the value of the datas property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the datas property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDatas().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LegalReferenceDataType }
     *
     *
     */
    public List<LegalReferenceDataType> getDatas() {
        if (datas == null) {
            datas = new ArrayList<LegalReferenceDataType>();
        }
        return this.datas;
    }

    /**
     * Gets the value of the legalReferenceTraces property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the legalReferenceTraces property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLegalReferenceTraces().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LegalReferenceKeyType }
     *
     *
     */
    public List<LegalReferenceKeyType> getLegalReferenceTraces() {
        if (legalReferenceTraces == null) {
            legalReferenceTraces = new ArrayList<LegalReferenceKeyType>();
        }
        return this.legalReferenceTraces;
    }

}
