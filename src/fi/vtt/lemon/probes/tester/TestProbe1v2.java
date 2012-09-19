/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probes.tester;

import osmo.common.log.Logger;

/**
 * Test probe to provide test data to the server-agent.
 * The same as TestProbe1 but with a higher precision.
 *
 * @author Teemu Kanstren
 */
public class TestProbe1v2 extends TestProbe {
  private final static Logger log = new Logger(TestProbe1v2.class);
  private int counter = 0;

  public TestProbe1v2() {
    super("Test Probe 1", 2);
  }

  public String measure() {
    String result = "test probe1v2 measure " + counter;
    counter++;
    log.debug("Performed measure:" + result);
    return result;
  }

}
