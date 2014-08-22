/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.rest;

import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.probes.tester.ConfigurableTestProbe;
import fi.vtt.lemon.probes.tester.TestProbe;
import fi.vtt.lemon.probes.tester.TestProbe1;
import fi.vtt.lemon.probes.tester.TestProbe2;
import fi.vtt.lemon.probes.tester.TestProbe3;
import fi.vtt.lemon.probes.tester.TimedTestProbe;
import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.Value;
import fi.vtt.lemon.server.external.RESTConst;
import fi.vtt.lemon.server.external.RestClient;
import fi.vtt.lemon.server.external.RestClient2;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import osmo.common.log.Logger;

import static fi.vtt.lemon.RabbitConst.PARAM_CONFIG;
import static fi.vtt.lemon.server.external.RESTConst.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/** @author Teemu Kanstren */
public class RESTCalls {
  private Collection<TestProbe> probes = new ArrayList<>();
  private RestClient2 rs2 = null;
  
  @BeforeClass
  public static void startServer() throws Exception {
    LemonServer.main(null);
  }
  
  @Before
  public void reset() {
    probes.clear();
    rs2 = new RestClient2("http://localhost:11111");
  }
  
  @After
  public void stopProbes() throws Exception {
    for (TestProbe probe : probes) {
      probe.stop();
    }
  }
  
  @Test
  public void availabilityOneProbe() throws Exception {
//    Logger.debug = true;

    TestProbe probe = new TestProbe1();
    probe.start();
    probes.add(probe);

    Thread.sleep(2222);
    String data = rs2.post(RESTConst.PATH_AVAILABILITY);
    JSONObject json = new JSONObject(data);
    JSONArray array = json.getJSONArray("availability");
    assertEquals("Availability size:"+array.toString(), 1, array.length());
    JSONObject item = array.getJSONObject(0);
    assertEquals("Registered probe", "MFW://Firewall/Bob1/Configuration file/Bobby1", item.getString(RabbitConst.PARAM_MEASURE_URI));
  }

  @Test
  public void availabilityThreeProbes() throws Exception {
//    Logger.debug = true;

    TestProbe probe1 = new TestProbe1();
    probe1.start();
    probes.add(probe1);

    TestProbe probe2 = new TestProbe2();
    probe2.start();
    probes.add(probe2);

    TestProbe probe3 = new TestProbe3();
    probe3.start();
    probes.add(probe3);

    Thread.sleep(2222);
    String data = rs2.post(RESTConst.PATH_AVAILABILITY);
    JSONObject json = new JSONObject(data);
    JSONArray array = json.getJSONArray("availability");
    assertEquals("Availability size:"+array.toString(), 3, array.length());
    JSONObject item1 = array.getJSONObject(0);
    JSONObject item2 = array.getJSONObject(1);
    JSONObject item3 = array.getJSONObject(2);
    System.out.println("data:\n"+json.toString());
    Collection<String> probes = new HashSet<>();
    probes.add(item1.getString(RabbitConst.PARAM_MEASURE_URI));
    probes.add(item2.getString(RabbitConst.PARAM_MEASURE_URI));
    probes.add(item3.getString(RabbitConst.PARAM_MEASURE_URI));
    assertTrue("Probe " + probe1.getMeasureURI() + " should be present", probes.contains(probe1.getMeasureURI()));
    assertTrue("Probe " + probe2.getMeasureURI() + " should be present", probes.contains(probe2.getMeasureURI()));
    assertTrue("Probe " + probe3.getMeasureURI() + " should be present", probes.contains(probe3.getMeasureURI()));
  }

  @Test
  public void frameworkInfo() throws Exception {
    String data = rs2.post(RESTConst.PATH_FRAMEWORK_INFO);
    JSONObject json = new JSONObject(data);
    assertEquals("Framework info", "LE-MON v0.1", json.getString("info"));
  }
  
  @Test
  public void history() throws Exception {
//    Logger.debug = true;

    TimedTestProbe probe = new TimedTestProbe(3);
    probe.start();
    probes.add(probe);

    Thread.sleep(2222);

    JSONObject json = new JSONObject();
    json.put(START_TIME, 1);
    json.put(END_TIME, 10);
    JSONArray bms = new JSONArray();
    json.put(BM_LIST, bms);
    JSONObject jo1 = new JSONObject();
    jo1.put(MEASURE_URI, "LM://Test/Timed/3");
    bms.put(jo1);

    JSONObject jo2 = new JSONObject();
    jo2.put(MEASURE_URI, "my_little_measure2");
    bms.put(jo2);

    JSONObject jo3 = new JSONObject();
    jo3.put(MEASURE_URI, "my_little_measure3");
    bms.put(jo3);

    String data = rs2.post(RESTConst.PATH_HISTORY, json);

    System.out.println("data:"+data);
  }

  @Test
  public void addRemoveMeasure() throws Exception {
    //Logger.debug = true;
    TestProbe probe = new ConfigurableTestProbe();
    probe.start();
    probes.add(probe);
    
    new FakeJettyStarter().start();
    
    JSONObject measureReq = new JSONObject();
    measureReq.put(MEASURE_URI, "le-mon://configurable/test-probe");
    rs2.post(PATH_SUBSCRIBE, measureReq);

    Thread.sleep(2222);

    List<Value> values = FakeClient.getValues();
    Value latest = values.get(values.size() - 1);
    assertEquals("Latest measure collected", "unmodified", latest.valueString());

    JSONObject json = new JSONObject();
    json.put(MEASURE_URI, "le-mon://configurable/test-probe");
    json.put(PARAM_CONFIG, "my little measure");
    rs2.post(PATH_ADD_MEASURE, json);

    System.out.println("set to my little measure");

    Thread.sleep(2222);

    values = FakeClient.getValues();
    latest = values.get(values.size() - 1);
    assertEquals("Latest measure collected", "my little measure", latest.valueString());

    json = new JSONObject();
    json.put(MEASURE_URI, "le-mon://configurable/test-probe");
    json.put(PARAM_CONFIG, "hello world");
    rs2.post(PATH_REMOVE_MEASURE, json);

    System.out.println("attempted to remove non-existent");

    Thread.sleep(2222);

    values = FakeClient.getValues();
    latest = values.get(values.size() - 1);
    assertEquals("Latest measure collected", "my little measure", latest.valueString());

    
    json = new JSONObject();
    json.put(MEASURE_URI, "le-mon://configurable/test-probe");
    json.put(PARAM_CONFIG, "my little measure");
    rs2.post(PATH_REMOVE_MEASURE, json);

    System.out.println("attempted to remove existent");

    Thread.sleep(2222);

    values = FakeClient.getValues();
    latest = values.get(values.size() - 1);
    assertEquals("Latest measure collected", "removed", latest.valueString());

    JSONObject unsubReq = new JSONObject();
    unsubReq.put(MEASURE_URI, "le-mon://configurable/test-probe");
    rs2.post(PATH_UNSUBSCRIBE, unsubReq);

    Thread.sleep(2222);
    
    int count = values.size();

    Thread.sleep(2222);

    int count2 = values.size();
    
    assertEquals("Number of values should not increase after unsubscribe", count, count2);
  }
}
