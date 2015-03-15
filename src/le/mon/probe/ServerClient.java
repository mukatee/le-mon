package le.mon.probe;

import osmo.common.log.Logger;

/**
 * Handles messaging with the le-mon server-agent.
 *
 * @author Teemu Kanstren
 */
public class ServerClient {
  private final static Logger log = new Logger(ServerClient.class);
  private final String server;

  /**
   * Constructor..
   *
   * @param url The server address..
   */
  public ServerClient(String url) {
    this.server = url;
  }

//  public boolean register(String measureURI, int precision) {
//    JSONObject json = new JSONObject();
//    try {
//      json.put(MSGTYPE, MSG_REGISTER);
//      json.put(PARAM_TIME, System.currentTimeMillis());
//      json.put(PARAM_MEASURE_URI, measureURI);
//      json.put(PARAM_PRECISION, precision);
//      RestClient2.sendPost(server, json);
//      return true;
//    } catch (Exception e) {
//      log.error("Error while sending msg to server", e);
//      return false;
//    }
//  }
//
//  public boolean unregister(String measureURI) {
//    JSONObject json = new JSONObject();
//    try {
//      json.put(MSGTYPE, MSG_UNREGISTER);
//      json.put(PARAM_TIME, System.currentTimeMillis());
//      json.put(PARAM_MEASURE_URI, measureURI);
//      RestClient2.sendPost(server, json);
//      return true;
//    } catch (Exception e) {
//      log.error("Error while sending msg to server", e);
//      return false;
//    }
//  }
//
//  /**
//   * Provides a measurement to the lemon server-agent.
//   *
//   * @param measureURI The measure ID.
//   * @param precision The probe precision.
//   * @param value The measurement value.
//   * @return True if no problem in trying to send the data..
//   */
//  public boolean measurement(String measureURI, int precision, String value) {
//    JSONObject json = new JSONObject();
//    try {
//      json.put(MSGTYPE, MSG_MEASUREMENT);
//      json.put(PARAM_TIME, System.currentTimeMillis());
//      json.put(PARAM_MEASURE_URI, measureURI);
//      json.put(PARAM_PRECISION, precision);
//      json.put(PARAM_VALUE, value);
//      RestClient2.sendPost(server, json);
//      return true;
//    } catch (Exception e) {
//      log.error("Error while sending msg to server", e);
//      return false;
//    }
//  }
//
//   /**
//   * Provides an event to the lemon server-agent.
//   *
//   * @param type Event type (define your own types).
//   * @param source Source of event (whatever floats your boat).
//   * @param msg The message of the event (give details, etc.)
//   * @return True if no problem in trying to send the data..
//   */
//  public boolean event(String type, String source, String msg) {
//    JSONObject json = new JSONObject();
//    try {
//      json.put(MSGTYPE, MSG_EVENT);
//      json.put(PARAM_TIME, System.currentTimeMillis());
//      json.put(PARAM_EVENT_TYPE, type);
//      json.put(PARAM_EVENT_SOURCE, source);
//      json.put(PARAM_EVENT_MSG, msg);
//      RestClient2.sendPost(server, json);
//      return true;
//    } catch (Exception e) {
//      log.error("Error while sending msg to server", e);
//      return false;
//    }
//  }
}
