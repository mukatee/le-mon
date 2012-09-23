/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import fi.vtt.lemon.probes.tester.TestProbe1;
import fi.vtt.lemon.rest.testclient.JerseyTestApp;
import fi.vtt.lemon.server.LemonServer;

import static fi.vtt.lemon.server.external.RESTConst.*;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.core.MediaType;

/** @author Teemu Kanstren */
public class Subscribe {
  public static void main(String[] args) throws Exception {
    LemonServer.main(null);
    JerseyTestApp tap = new JerseyTestApp();
    tap.start();

    TestProbe1 tp1 = new TestProbe1();
    tp1.start();

    Client client = Client.create();
    WebResource wr = client.resource("http://localhost:11111/rest/");
    WebResource.Builder path = wr.path(PATH_SUBSCRIBE).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
    JSONObject json = new JSONObject();
    json.put(MEASURE_URI, "MFW://Firewall/Bob1/Configuration file/Bobby1");
    ClientResponse data = path.post(ClientResponse.class, json.toString());
    
    System.out.println("data:"+data);
  }
}
