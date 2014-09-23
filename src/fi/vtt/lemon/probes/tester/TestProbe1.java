package fi.vtt.lemon.probes.tester;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.MsgConst;
import fi.vtt.lemon.probe.ServerClient;
import fi.vtt.lemon.probe.measurement.MeasurementProvider;
import osmo.common.log.Logger;

/**
 * Test probe to provide test data to the server-agent.
 *
 * @author Teemu Kanstren
 */
public class TestProbe1 extends TestProbe {
  private final static Logger log = new Logger(TestProbe1.class);
  private int counter = 0;

  public TestProbe1() {
    super("MFW://Firewall/Bob1/Configuration file/Bobby1", 1);
  }

  public String measure() {
    String result = "test probe1 measure " + counter + " test to see if going over the limit of 100 characters ends up as truncating the text at a specific point";
    counter++;
    log.debug("Performed measure:" + result);
    return result;
  }

  public static void main(String[] args) throws Exception {
    MeasurementProvider mp = new MeasurementProvider(new ServerClient(Config.getString(MsgConst.BROKER_ADDRESS, "::1")));
    mp.startMeasuring(new TestProbe1());
  }
}
