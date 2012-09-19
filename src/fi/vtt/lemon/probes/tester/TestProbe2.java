/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probes.tester;

/**
 * Test probe to provide test data to the server-agent.
 *
* @author Teemu Kanstren
 */
public class TestProbe2 extends TestProbe {
  private int counter = 0;

  public TestProbe2() {
    super("MFW://Spam Filter/Bob2/configuration file/Bobby", 1);
  }

  public String measure() {
//    String result = "test probe2 measure " + counter;
    String result = Integer.toString( counter % 100 );
    counter++;
    return result;
  }
}
