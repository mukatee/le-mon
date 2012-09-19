
/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.mfwclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


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
 *         &lt;element ref="{http://www.bugyobeyond.org/SAC_MFW/}BMResultRequest" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="sac_id" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "bmResultRequest"
})
@XmlRootElement(name = "BMResultsRequest")
public class BMResultsRequest {

    @XmlElement(name = "BMResultRequest", required = true)
    protected List<BMResultRequest> bmResultRequest;
    @XmlAttribute(name = "sac_id", required = true)
    protected long sacId;

    /**
     * Gets the value of the bmResultRequest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the bmResultRequest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getBMResultRequest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link BMResultRequest }
     * 
     * 
     */
    public List<BMResultRequest> getBMResultRequest() {
        if (bmResultRequest == null) {
            bmResultRequest = new ArrayList<BMResultRequest>();
        }
        return this.bmResultRequest;
    }

    /**
     * Gets the value of the sacId property.
     * 
     */
    public long getSacId() {
        return sacId;
    }

    /**
     * Sets the value of the sacId property.
     * 
     */
    public void setSacId(long value) {
        this.sacId = value;
    }

}
