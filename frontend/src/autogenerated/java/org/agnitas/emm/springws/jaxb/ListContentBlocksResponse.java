/*

    Copyright (C) 2019 AGNITAS AG (https://www.agnitas.org)

    This program is free software: you can redistribute it and/or modify it under the terms of the GNU Affero General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
    This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.

*/


package org.agnitas.emm.springws.jaxb;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="ContentBlock" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;all>
 *                   &lt;element name="contentID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *                   &lt;element name="targetID" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                   &lt;element name="order" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *                 &lt;/all>
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "contentBlock"
})
@XmlRootElement(name = "ListContentBlocksResponse")
@SuppressWarnings("all")
public class ListContentBlocksResponse {

    @XmlElement(name = "ContentBlock")
    protected List<ListContentBlocksResponse.ContentBlock> contentBlock;

    /**
     * Gets the value of the contentBlock property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the contentBlock property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getContentBlock().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ListContentBlocksResponse.ContentBlock }
     * 
     * 
     */
    public List<ListContentBlocksResponse.ContentBlock> getContentBlock() {
        if (contentBlock == null) {
            contentBlock = new ArrayList<ListContentBlocksResponse.ContentBlock>();
        }
        return this.contentBlock;
    }


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
     *         &lt;element name="contentID" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
     *         &lt;element name="targetID" type="{http://www.w3.org/2001/XMLSchema}int"/>
     *         &lt;element name="order" type="{http://www.w3.org/2001/XMLSchema}int"/>
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
    public static class ContentBlock {

        protected int contentID;
        @XmlElement(required = true)
        protected String name;
        protected int targetID;
        protected int order;

        /**
         * Gets the value of the contentID property.
         * 
         */
        public int getContentID() {
            return contentID;
        }

        /**
         * Sets the value of the contentID property.
         * 
         */
        public void setContentID(int value) {
            this.contentID = value;
        }

        /**
         * Gets the value of the name property.
         * 
         * @return
         *     possible object is
         *     {@link String }
         *     
         */
        public String getName() {
            return name;
        }

        /**
         * Sets the value of the name property.
         * 
         * @param value
         *     allowed object is
         *     {@link String }
         *     
         */
        public void setName(String value) {
            this.name = value;
        }

        /**
         * Gets the value of the targetID property.
         * 
         */
        public int getTargetID() {
            return targetID;
        }

        /**
         * Sets the value of the targetID property.
         * 
         */
        public void setTargetID(int value) {
            this.targetID = value;
        }

        /**
         * Gets the value of the order property.
         * 
         */
        public int getOrder() {
            return order;
        }

        /**
         * Sets the value of the order property.
         * 
         */
        public void setOrder(int value) {
            this.order = value;
        }

    }

}
