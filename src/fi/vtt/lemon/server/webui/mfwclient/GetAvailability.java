
/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.mfwclient;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
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
 *       &lt;attribute name="sac_id" use="required" type="{http://www.w3.org/2001/XMLSchema}long" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "getAvailability")
public class GetAvailability {

    @XmlAttribute(name = "sac_id", required = true)
    protected long sacId;

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
