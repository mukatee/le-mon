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

package fi.vtt.lemon.mfw.unittests.agent.probe;

import fi.vtt.lemon.common.Const;
import fi.vtt.lemon.common.ProbeConfiguration;
import fi.vtt.lemon.probe.plugins.xmlrpc.ProbeAgentConfig;
import fi.vtt.lemon.probe.plugins.xmlrpc.ProbeAgentImpl;
import fi.vtt.lemon.probe.shared.Probe;
import fi.vtt.lemon.probes.tester.TestProbe;
import fi.vtt.lemon.probes.tester.TestProbe1;
import fi.vtt.lemon.mfw.unittests.TestUtils;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

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
    addProperty(Const.MFW_SERVER_URL_KEY, serverUrl);
    addProperty(Const.PROBE_AGENT_PORT_KEY, "5555");
    addProperty(Const.KEEP_ALIVE_INTERVAL, 1);
    InputStream in = configStream();
    ProbeAgentConfig config = new ProbeAgentConfig(in);
    assertEquals("Probe agent port from properties file", 5555, config.getProbeAgentServerPort());
    assertNotNull("Probe agent server created from properties file", config.getDestination());
  }

  @Test
  public void probeConfigRead() throws Exception {
    Properties props = new Properties();
    props.put(Const.TEST_PROBE_AGENT_CONFIG_PREFIX+".probe1.script", "hello world");
    props.put(Const.TEST_PROBE_AGENT_CONFIG_PREFIX+".probe1.length", "33");
    props.put(Const.TEST_PROBE_AGENT_CONFIG_PREFIX+".probe2.ignorance", "bliss");
    props.put(Const.TEST_PROBE_AGENT_CONFIG_PREFIX+".probe1.width", "11");

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

  /**
   * Test setting the probe configuration through a server-agent and reading it back, and
   * re-setting it...
   *
   * Possible issue: practically this all happens inside the same VM and the client and
   * server share the data structures. Since get and set are done each time it should not
   * be an issue but ...
   *
   * @throws Exception
   */
  @Test
  public void probeConfigSet() throws Exception {
    Thread.sleep(500);

    ProbeAgentImpl pai = null;
    Map<Long,Probe> probes = pai.getProbes();
    assertEquals("Number of registered probes", 6, probes.size());

    Probe probe1 = null;
    long probe1id = -1;
    Set<Map.Entry<Long,Probe>> entries = probes.entrySet();
    for (Map.Entry<Long, Probe> entry : entries) {
      long key = entry.getKey();
      Probe probe = entry.getValue();
      if (probe instanceof TestProbe1) {
        probe1 = probe;
        probe1id = key;
        break;
      }
    }
    assertNotNull("Probe1 should be registered", probe1);

    Collection<ProbeConfiguration> tmpConfig = pai.getConfigurationParameters(probe1id);
    Map<String, String> oldConfig = convertConfiguration(tmpConfig);
    String height = oldConfig.get("height");
    assertEquals("Configuration parameter read", "10 meters", height);
    assertEquals("Probe1 should have two configuration parameters initially set", 2, oldConfig.size());

    oldConfig.put("width", "20 meters");
    pai.setConfiguration(probe1id, oldConfig);

    tmpConfig = pai.getConfigurationParameters(probe1id);
    Map<String, String> newConfig = convertConfiguration(tmpConfig);
    height = newConfig.get("height");
    String width = newConfig.get("width");
    assertEquals("Configuration parameter read", "10 meters", height);
    assertEquals("Probe1 should have one configuration parameters initially set", 2, newConfig.size());
    assertEquals("Configuration parameter read", "20 meters", width);

    Map<String, String> cleanConfig = new HashMap<String, String>();
    cleanConfig.put("fluid", "liquid stuff");
    pai.setConfiguration(probe1id, cleanConfig);
    tmpConfig = pai.getConfigurationParameters(probe1id);
    newConfig = convertConfiguration(tmpConfig);
    assertEquals("New configuration parameter read", newConfig.get("fluid"), "liquid stuff");
  }

  private Map<String, String> convertConfiguration(Collection<ProbeConfiguration> config) {
    Map<String, String> result = new HashMap<String, String>();
    for (ProbeConfiguration pc : config) {
      result.put(pc.getName(), pc.getValue());
    }
    return result;
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
