
package com.soapboxrace.jaxb.http;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for FriendResult complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="FriendResult">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="persona" type="{}FriendPersona" minOccurs="0"/>
 *         &lt;element name="result" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "FriendResult", propOrder = {
    "persona",
    "result"
})
public class FriendResult {

    protected FriendPersona persona;
    protected int result;

    /**
     * Gets the value of the persona property.
     * 
     * @return
     *     possible object is
     *     {@link FriendPersona }
     *     
     */
    public FriendPersona getPersona() {
        return persona;
    }

    /**
     * Sets the value of the persona property.
     * 
     * @param value
     *     allowed object is
     *     {@link FriendPersona }
     *     
     */
    public void setPersona(FriendPersona value) {
        this.persona = value;
    }

    /**
     * Gets the value of the result property.
     * 
     */
    public int getResult() {
        return result;
    }

    /**
     * Sets the value of the result property.
     * 
     */
    public void setResult(int value) {
        this.result = value;
    }

}
