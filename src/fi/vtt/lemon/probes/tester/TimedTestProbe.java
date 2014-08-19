/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.probes.tester;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.probe.ServerClient;
import fi.vtt.lemon.probe.measurement.MeasurementProvider;
import osmo.common.log.Logger;

/**
 * Test probe to provide test data to the server-agent.
 *
 * @author Teemu Kanstren
 */
public class TimedTestProbe extends TestProbe {
  private final static Logger log = new Logger(TimedTestProbe.class);
  private int counter = 0;
  private final int n;

  public TimedTestProbe(int n) {
    super("LM://Test/Timed/"+n, 1);
    this.n = n;
  }

  public String measure() {
    String result = "timed measure nr."+counter;
    counter++;
    log.debug("Performed measure:" + result);
    try {
      if (counter >= n) stop();
    } catch (Exception e) {
      //TODO: error handling
      e.printStackTrace();
    }
    return result;
  }

  public static void main(String[] args) throws Exception {
    MeasurementProvider mp = new MeasurementProvider(new ServerClient(Config.getString(RabbitConst.BROKER_ADDRESS, "::1")));
    mp.startMeasuring(new TimedTestProbe(3));
  }
}
