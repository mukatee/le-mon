/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.rest;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.MsgConst;
import fi.vtt.lemon.probe.ProbeServer;
import fi.vtt.lemon.probes.tester.ConfigurableTestProbe;
import fi.vtt.lemon.probes.tester.TestProbe;
import fi.vtt.lemon.probes.tester.TestProbe1;
import fi.vtt.lemon.probes.tester.TestProbe2;
import fi.vtt.lemon.probes.tester.TestProbe3;
import fi.vtt.lemon.probes.tester.TimedTestProbe;
import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.data.Value;
import fi.vtt.lemon.server.rest.RESTConst;
import fi.vtt.lemon.server.rest.RestClient2;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import osmo.common.log.Logger;

import static fi.vtt.lemon.MsgConst.PARAM_CONFIG;
import static fi.vtt.lemon.server.rest.RESTConst.*;
import static org.testng.Assert.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;

/** @author Teemu Kanstren */
public class RESTCalls {
  private Collection<TestProbe> probes = new ArrayList<>();
  private RestClient2 rs2 = null;
  
  @BeforeClass
  public static void startServer() throws Exception {
    Logger.consoleLevel = Level.FINE;
    Logger.packageName = "f.v.l";
    LemonServer.main(null);
    ProbeServer.start();
  }
  
  @BeforeMethod
  public void reset() {
    LemonServer.reset();
    probes.clear();
    int port = Config.getInt(REST_SERVER_SERVER_PORT, 11112);
    String url = "http://localhost:"+port;
    rs2 = new RestClient2(url);
  }
  
  @AfterTest
  public void stopProbes() throws Exception {
    for (TestProbe probe : probes) {
      if (!probe.isStopped()) probe.stop();
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
    assertEquals(array.length(), 1, "Availability size:"+array.toString());
    JSONObject item = array.getJSONObject(0);
    assertEquals(item.getString(MsgConst.PARAM_MEASURE_URI), "MFW://Firewall/Bob1/Configuration file/Bobby1", "Registered probe");
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
    assertEquals(array.length(), 3, "Availability size:"+array.toString());
    JSONObject item1 = array.getJSONObject(0);
    JSONObject item2 = array.getJSONObject(1);
    JSONObject item3 = array.getJSONObject(2);
//    System.out.println("data:\n"+json.toString());
    Collection<String> probes = new HashSet<>();
    probes.add(item1.getString(MsgConst.PARAM_MEASURE_URI));
    probes.add(item2.getString(MsgConst.PARAM_MEASURE_URI));
    probes.add(item3.getString(MsgConst.PARAM_MEASURE_URI));
    assertTrue(probes.contains(probe1.getMeasureURI()), "Probe " + probe1.getMeasureURI() + " should be present");
    assertTrue(probes.contains(probe2.getMeasureURI()), "Probe " + probe2.getMeasureURI() + " should be present");
    assertTrue(probes.contains(probe3.getMeasureURI()), "Probe " + probe3.getMeasureURI() + " should be present");
  }

  @Test
  public void frameworkInfo() throws Exception {
    String data = rs2.post(RESTConst.PATH_FRAMEWORK_INFO);
    JSONObject json = new JSONObject(data);
    assertEquals(json.getString("info"), "LE-MON v0.1", "Framework info");
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
    assertEquals(latest.valueString(), "unmodified", "Latest measure collected");

    JSONObject json = new JSONObject();
    json.put(MEASURE_URI, "le-mon://configurable/test-probe");
    json.put(PARAM_CONFIG, "my little measure");
    rs2.post(PATH_ADD_MEASURE, json);

    System.out.println("set to my little measure");

    Thread.sleep(2222);

    values = FakeClient.getValues();
    latest = values.get(values.size() - 1);
    assertEquals(latest.valueString(), "my little measure", "Latest measure collected");

    json = new JSONObject();
    json.put(MEASURE_URI, "le-mon://configurable/test-probe");
    json.put(PARAM_CONFIG, "hello world");
    rs2.post(PATH_REMOVE_MEASURE, json);

//    System.out.println("attempted to remove non-existent");

    Thread.sleep(2222);

    values = FakeClient.getValues();
    latest = values.get(values.size() - 1);
    assertEquals(latest.valueString(), "my little measure", "Latest measure collected");
  
    json = new JSONObject();
    json.put(MEASURE_URI, "le-mon://configurable/test-probe");
    json.put(PARAM_CONFIG, "my little measure");
    rs2.post(PATH_REMOVE_MEASURE, json);

//    System.out.println("attempted to remove existent");

    Thread.sleep(2222);

    values = FakeClient.getValues();
    latest = values.get(values.size() - 1);
    assertEquals(latest.valueString(), "removed", "Latest measure collected");

    JSONObject unsubReq = new JSONObject();
    unsubReq.put(MEASURE_URI, "le-mon://configurable/test-probe");
    rs2.post(PATH_UNSUBSCRIBE, unsubReq);

    Thread.sleep(2222);
    
    int count = values.size();

    Thread.sleep(2222);

    int count2 = values.size();
    
    assertEquals(count, count2, "Number of values should not increase after unsubscribe");
  }
}
