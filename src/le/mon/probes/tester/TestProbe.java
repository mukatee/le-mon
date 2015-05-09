package le.mon.probes.tester;

import le.mon.probe.Probe;
import le.mon.probe.ProbeServer;
import le.mon.probe.measurement.MeasurementProvider;
import osmo.common.log.Logger;

/**
 * Base class to create test probes that provide test-data to the server-agent.
 *
 * @author Teemu Kanstren
 */
public class TestProbe implements Probe {
  private final static Logger log = new Logger(TestProbe.class);
  private final String result;
  private final String measureURI;
  private final int precision;
  private MeasurementProvider mp;
  private boolean stopped = false;

  public TestProbe(String result, String measureURI, int precision) {
    this.result = result;
    this.measureURI = measureURI;
    this.precision = precision;
  }

  public TestProbe(String measureURI, int precision) {
    this.result = null;
    this.measureURI = measureURI;
    this.precision = precision;
  }

  public boolean isStopped() {
    return stopped;
  }

  public void start() throws Exception {
    mp = new MeasurementProvider();
    mp.startMeasuring(this);
    ProbeServer.addProbe(this);
  }

  public void stop() throws Exception {
    stopped = true;
    mp.stop();
    ProbeServer.removeProbe(this);
  }

  public String getMeasureURI() {
    return measureURI;
  }

  public int getPrecision() {
    return precision;
  }

  public String measure() {
    log.debug("Testprobe provides measure:" + result);
    return result;
  }

  @Override
  public void addMeasure(String config) {
    log.debug("Testprobe received request to add measure:" + config);
  }

  @Override
  public void removeMeasure(String config) {
    log.debug("Testprobe received request to remove measure:" + config);
  }
}
