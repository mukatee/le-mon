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

package fi.vtt.lemon.unittests.agent.probe;

import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.probe.ProbeAgentConfig;
import fi.vtt.lemon.probes.tester.TestProbe;
import fi.vtt.lemon.unittests.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.*;

/**
 * Tests related to checking the probe startup configuration from a properties file.
 *
 * @author Teemu Kanstren
 */
public class ProbeConfigurationTests {
  private static final String ln = "\n";
  private String input = "";

  @Before
  public void setup() {
    input = "";
  }

  @Test
  public void probeAgentConfigRead() throws Exception {
    String serverUrl = "http://localhost:8005/xmlrpc";
    addProperty(RabbitConst.SERVER_URL, serverUrl);
    InputStream in = configStream();
    ProbeAgentConfig config = new ProbeAgentConfig(in);
    assertNotNull("Probe agent server created from properties file", config.getServerClient());
  }

  @Test
  public void probeConfigRead() throws Exception {
    Properties props = new Properties();
    props.put(RabbitConst.TEST_PROBE_AGENT_CONFIG_PREFIX+".probe1.script", "hello world");
    props.put(RabbitConst.TEST_PROBE_AGENT_CONFIG_PREFIX+".probe1.length", "33");
    props.put(RabbitConst.TEST_PROBE_AGENT_CONFIG_PREFIX+".probe2.ignorance", "bliss");
    props.put(RabbitConst.TEST_PROBE_AGENT_CONFIG_PREFIX+".probe1.width", "11");

    TestProbe probe1 = null;
    Properties actual = probe1.getProperties();
    assertEquals("Number of properties read for probe 1", 3, actual.size());
    assertEquals("Configuration property value", "hello world", actual.getProperty("script"));
    assertEquals("Configuration property value", "33", actual.getProperty("length"));
    assertEquals("Configuration property value", "11", actual.getProperty("width"));

    TestProbe probe2 = null;
    actual = probe2.getProperties();
    assertEquals("Number of properties read for probe 2", 1, actual.size());
    assertEquals("Configuration property value", "bliss", actual.getProperty("ignorance"));
  }

  @Test
  public void probeConfigGet() {

  }

  private void addProperty(String key, Object value) {
    String postFix = key + "=" + value + ln;
    input += postFix;
  }

  private InputStream configStream() {
    return TestUtils.streamFor(input);
  }
}
