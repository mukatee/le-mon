package le.mon.probe.tasks;

import le.mon.MsgConst;
import le.mon.probe.Probe;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

/**
 * Defines a task for the adding a new measurement to the probe.
 *
 * @author Teemu Kanstren
 */
public class RemoveMeasureProcessor implements Runnable {
  private final static Logger log = new Logger(RemoveMeasureProcessor.class);
  /** The message data received. */
  private final JSONObject json;
  private final Probe probe;

  public RemoveMeasureProcessor(Probe probe, JSONObject json) {
    this.probe = probe;
    this.json = json;
  }

  /**
   * This where the task does the magic, invoked by the thread pool executor.
   */
  @Override
  public void run() {
    String config = null;
    try {
      long time = json.getLong(MsgConst.PARAM_TIME);
      config = json.getString(MsgConst.PARAM_CONFIG);
      String measureURI = json.getString(MsgConst.PARAM_MEASURE_URI);
      if (!measureURI.equals(probe.getMeasureURI())) {
        //TODO: this would not be necessary if probes used own msg queues, which they need to be modified to do
        log.debug("Not a request for this probe:'" + measureURI + "' I have '" + probe.getMeasureURI() + "'");
        return;
      }
      probe.removeMeasure(config);
      log.debug("Measurent removed from probe:" + config);
    } catch (Exception e) {
      log.error("Failed to remove measure:" + config, e);
    }
  }
}
