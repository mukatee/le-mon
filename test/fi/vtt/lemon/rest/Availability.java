/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.probes.tester.TestProbe;
import fi.vtt.lemon.probes.tester.TestProbe1;
import fi.vtt.lemon.probes.tester.TestProbe2;
import fi.vtt.lemon.probes.tester.TestProbe3;
import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.external.JerseyApp;
import fi.vtt.lemon.server.external.RESTConst;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import osmo.common.log.Logger;

import static org.junit.Assert.*;

import javax.ws.rs.core.MediaType;

/** @author Teemu Kanstren */
public class Availability {
  @Test
  public void oneProbe() throws Exception {
//    Logger.debug = true;
    LemonServer.main(null);

    TestProbe probe = new TestProbe1();
    probe.start();

    Client client = Client.create();
    WebResource wr = client.resource("http://localhost:11111/rest/");
    Thread.sleep(2222);
    String data = wr.path(RESTConst.PATH_AVAILABILITY).accept(MediaType.APPLICATION_JSON).get(String.class);
    JSONObject json = new JSONObject(data);
    JSONArray array = json.getJSONArray("availability");
    assertEquals("Availability size:"+array.toString(), 1, array.length());
    JSONObject item = array.getJSONObject(0);
    assertEquals("Registered probe", "MFW://Firewall/Bob1/Configuration file/Bobby1", item.getString(RabbitConst.PARAM_MEASURE_URI));
  }

  @Test
  public void threeProbes() throws Exception {
//    Logger.debug = true;
    LemonServer.main(null);

    TestProbe probe1 = new TestProbe1();
    probe1.start();

    TestProbe probe2 = new TestProbe2();
    probe2.start();

    TestProbe probe3 = new TestProbe3();
    probe3.start();

    Client client = Client.create();
    WebResource wr = client.resource("http://localhost:11111/rest/");
    Thread.sleep(2222);
    String data = wr.path(RESTConst.PATH_AVAILABILITY).accept(MediaType.APPLICATION_JSON).get(String.class);
    JSONObject json = new JSONObject(data);
    JSONArray array = json.getJSONArray("availability");
    assertEquals("Availability size:"+array.toString(), 3, array.length());
    JSONObject item1 = array.getJSONObject(0);
    JSONObject item2 = array.getJSONObject(1);
    JSONObject item3 = array.getJSONObject(2);
    System.out.println("data:\n"+json.toString());
    assertEquals("Registered probe 1", "MFW://Communication protocol/Bob3/Encryption key length/Bobby3", item1.getString(RabbitConst.PARAM_MEASURE_URI));
    assertEquals("Registered probe 2", "MFW://Firewall/Bob1/Configuration file/Bobby1", item2.getString(RabbitConst.PARAM_MEASURE_URI));
    assertEquals("Registered probe 3", "MFW://Spam Filter/Bob2/configuration file/Bobby", item3.getString(RabbitConst.PARAM_MEASURE_URI));
  }
}
