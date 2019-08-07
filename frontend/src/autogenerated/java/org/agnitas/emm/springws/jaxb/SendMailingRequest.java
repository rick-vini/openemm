/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/


package org.agnitas.emm.springws.jaxb;

import java.util.Date;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="mailingID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="recipientsType" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="sendDate" type="{http://www.w3.org/2001/XMLSchema}dateTime"/>
 *         &lt;element name="blocksize" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *         &lt;element name="stepping" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {

})
@XmlRootElement(name = "SendMailingRequest")
@SuppressWarnings("all")
public class SendMailingRequest {

    protected int mailingID;
    @XmlElement(required = true)
    protected String recipientsType;
    @XmlElement(required = true, type = String.class)
    @XmlJavaTypeAdapter(DateAdapter.class)
    @XmlSchemaType(name = "dateTime")
    protected Date sendDate;
    protected Integer blocksize;
    protected Integer stepping;

    /**
     * Gets the value of the mailingID property.
     * 
     */
    public int getMailingID() {
        return mailingID;
    }

    /**
     * Sets the value of the mailingID property.
     * 
     */
    public void setMailingID(int value) {
        this.mailingID = value;
    }

    /**
     * Gets the value of the recipientsType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRecipientsType() {
        return recipientsType;
    }

    /**
     * Sets the value of the recipientsType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRecipientsType(String value) {
        this.recipientsType = value;
    }

    /**
     * Gets the value of the sendDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public Date getSendDate() {
        return sendDate;
    }

    /**
     * Sets the value of the sendDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSendDate(Date value) {
        this.sendDate = value;
    }

    /**
     * Gets the value of the blocksize property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getBlocksize() {
        return blocksize;
    }

    /**
     * Sets the value of the blocksize property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setBlocksize(Integer value) {
        this.blocksize = value;
    }

    /**
     * Gets the value of the stepping property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getStepping() {
        return stepping;
    }

    /**
     * Sets the value of the stepping property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStepping(Integer value) {
        this.stepping = value;
    }

}
