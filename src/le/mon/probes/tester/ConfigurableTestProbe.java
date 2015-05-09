package le.mon.probes.tester;

import le.mon.probe.measurement.MeasurementProvider;
import osmo.common.log.Logger;

/**
 * Test probe to provide test data to the server-agent.
 *
 * @author Teemu Kanstren
 */
public class ConfigurableTestProbe extends TestProbe {
  private final static Logger log = new Logger(ConfigurableTestProbe.class);
  private String measure = "unmodified";

  public ConfigurableTestProbe() {
    super("le-mon://configurable/test-probe", 1);
  }

  public String measure() {
    log.debug("Performed measure:" + measure);
    return measure;
  }

  @Override
  public void addMeasure(String config) {
    log.info("Setting measure to:" + config);
    measure = config;
  }

  @Override
  public void removeMeasure(String config) {
    if (!measure.equals(config)) {
      log.debug("Attempt to remove non-existent measure:" + config);
      return;
    }
    measure = "removed";
  }

  public static void main(String[] args) throws Exception {
    MeasurementProvider mp = new MeasurementProvider();
    ConfigurableTestProbe probe = new ConfigurableTestProbe();
    mp.startMeasuring(probe);
    probe.addMeasure("new measurement");
    probe.removeMeasure("undefined");
    probe.removeMeasure("new measurement");

  }
}
