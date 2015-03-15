package le.mon.probes.tester;

import le.mon.Config;
import le.mon.MsgConst;
import le.mon.probe.ServerClient;
import le.mon.probe.measurement.MeasurementProvider;
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
    MeasurementProvider mp = new MeasurementProvider();
    mp.startMeasuring(new TimedTestProbe(3));
  }
}
