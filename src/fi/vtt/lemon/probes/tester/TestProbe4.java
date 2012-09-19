/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probes.tester;

/**
 * Test probe to provide test data to the server-agent.
 *
 * @author Teemu Kanstren
 */
public class TestProbe4 extends TestProbe {
  private int counter = 0;

  public TestProbe4() {
    super(null, "Test Probe 4", 1);
  }

  public String measure() {
    String result = "test probe4 measure " + counter;
    counter++;
    return result;
  }

}