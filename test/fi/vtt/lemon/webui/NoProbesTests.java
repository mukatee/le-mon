/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.webui;

import com.gargoylesoftware.htmlunit.ThreadedRefreshHandler;
import net.sourceforge.jwebunit.htmlunit.HtmlUnitTestingEngineImpl;
import net.sourceforge.jwebunit.junit.WebTester;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static fi.vtt.lemon.unittests.TestUtils.*;
import static fi.vtt.lemon.webui.WebTestUtils.*;
import static org.junit.Assert.*;

/**
 * @author Teemu Kanstren
 */
public class NoProbesTests {
  private WebTester t = null;
  private static String WEB_UI_PORT = "8080";

  @BeforeClass
  public static void setupSuite() throws Exception {
    String port = System.getenv("MFW_WEBUI_PORT");
    if (port != null) {
      WEB_UI_PORT = port;
    }
    dropDatabase();
    startRemoteServer();
  }

  @AfterClass
  public static void teardown() {
    invokeRestShutdown(WEB_UI_PORT);
  }

  @Before
  public void setup() throws Exception {
    t = new WebTester();
    t.setBaseUrl("http://localhost:"+WEB_UI_PORT+"/");
    HtmlUnitTestingEngineImpl engine = (HtmlUnitTestingEngineImpl) t.getTestingEngine();
    engine.setRefreshHandler(new ThreadedRefreshHandler());
  }

  @Test
  public void noValues() throws Exception {
    goToHistoryFrame(t);
    t.assertTablePresent(MEASUREMENTS_TABLE);
    String pageSource = t.getPageSource();
    assertTrue("History page should be empty when started on empty database:"+pageSource, pageSource.contains("No Records Found"));
    t.assertTableRowCountEquals(MEASUREMENTS_TABLE, 0);
//    Table history = t.getTable("history_table");
  }

  @Test
  public void noProbes() throws Exception {
    goToProbeFrame(t);
    String pageSource = t.getPageSource();
    t.assertTablePresent(PROBES_TABLE);
    assertTrue("Probe page should be empty when started on empty database:"+pageSource, pageSource.contains("No Records Found"));
    t.assertTableRowCountEquals(PROBES_TABLE, 0);
    t.assertTableRowCountEquals(PROBE_INFO_TABLE, 0);
    t.assertTableRowCountEquals(AVAILABLE_BM_TABLE, 0);
  }

  @Test
  public void noEvents() throws Exception {
    goToEventFrame(t);
    String pageSource = t.getPageSource();
    t.assertTablePresent(EVENTS_TABLE);
    assertTrue("Event page should be empty when started on empty database:"+pageSource, pageSource.contains("No Records Found"));
    t.assertTableRowCountEquals(EVENTS_TABLE, 0);
  }
}
