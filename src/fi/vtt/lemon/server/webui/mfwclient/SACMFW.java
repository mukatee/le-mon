/*
 * Copyright (C) 2010-2011 VTT Technical Research Centre of Finland.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package fi.vtt.lemon.server.webui.mfwclient;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * This class was generated by Apache CXF 2.3.0
 * Tue Jan 25 14:11:33 EET 2011
 * Generated source version: 2.3.0
 * 
 */
 
@WebService(targetNamespace = "http://www.bugyobeyond.org/SAC_MFW/", name = "SAC_MFW")
@XmlSeeAlso({ObjectFactory.class})
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface SACMFW {

    @WebResult(name = "ProbeParameters", targetNamespace = "http://www.bugyobeyond.org/SAC_MFW/", partName = "parameters")
    @WebMethod(action = "http://www.bugyobeyond.org/SAC_MFW/getProbeParameters")
    public ProbeParameters getProbeParameters(
        @WebParam(partName = "parameters", name = "ProbeParametersRequest", targetNamespace = "http://www.bugyobeyond.org/SAC_MFW/")
        ProbeParametersRequest parameters
    );

    @WebResult(name = "OperationResult", targetNamespace = "http://www.bugyobeyond.org/SAC_MFW/", partName = "parameters")
    @WebMethod(action = "http://www.bugyobeyond.org/SAC_MFW/setProbeParameters")
    public OperationResult setProbeParameters(
        @WebParam(partName = "parameters", name = "ProbeParameterValues", targetNamespace = "http://www.bugyobeyond.org/SAC_MFW/")
        ProbeParameterValues parameters
    );

    @WebResult(name = "Availability", targetNamespace = "http://www.bugyobeyond.org/SAC_MFW/", partName = "parameters")
    @WebMethod(action = "http://www.bugyobeyond.org/SAC_MFW/getAvailability")
    public Availability getAvailability(
        @WebParam(partName = "parameters", name = "getAvailability", targetNamespace = "http://www.bugyobeyond.org/SAC_MFW/")
        GetAvailability parameters
    );

    @WebResult(name = "OperationResult", targetNamespace = "http://www.bugyobeyond.org/SAC_MFW/", partName = "parameters")
    @WebMethod(action = "http://www.bugyobeyond.org/SAC_MFW/getBMResults")
    public OperationResult getBMResults(
        @WebParam(partName = "parameters", name = "BMResultsRequest", targetNamespace = "http://www.bugyobeyond.org/SAC_MFW/")
        BMResultsRequest parameters
    );

    @WebResult(name = "MFW", targetNamespace = "http://www.bugyobeyond.org/SAC_MFW/", partName = "parameters")
    @WebMethod(action = "http://www.bugyobeyond.org/SAC_MFW/getMFW")
    public MFW getMFW(
        @WebParam(partName = "parameters", name = "getMFW", targetNamespace = "http://www.bugyobeyond.org/SAC_MFW/")
        GetMFW parameters
    );
}
