package fi.vtt.lemon;

/**
 * Contains shared constant values for the different MFW implementation elements.
 *
 * @author Teemu Kanstren
 */
public class MsgConst {
  //describes the base measure "class" for a probe
  public static final String PROBE_BM_CLASS = "bm_class";
  //base measure name for a probe
  public static final String PROBE_BM_NAME = "bm_name";
  //base measure description for a probe
  public static final String PROBE_BM_DESCRIPTION = "bm_description";
  //the name of a probe
  public static final String PROBE_NAME = "description";
  //precision of a probe, integer, higher is better probe with better measurements
  public static final String PROBE_PRECISION = "precision";
  //the name of the target of measurement for a probe
  public static final String PROBE_TARGET_NAME = "target_name";
  //the type of the target of measurement for a probe
  public static final String PROBE_TARGET_TYPE = "target_type";
  public static final String PROBE_URL = "probe_url";
  public static final String THREAD_POOL_SIZE = "thread_pool_size";
  public static final String TASK_TIMEOUT = "task_timeout";

  public static final String PARAM_TIME = "time";
  public static final String PARAM_MEASURE_URI = "measure_uri";
  public static final String PARAM_PRECISION = "precision";
  public static final String PARAM_PROBE_URL = "probe_url";
  public static final String PARAM_VALUE = "value";
  public static final String PARAM_EVENT_TYPE = "event_type";
  public static final String PARAM_EVENT_SOURCE = "event_source";
  public static final String PARAM_EVENT_MSG = "event_msg";
  public static final String PARAM_CONFIG = "config";
  public static final String PARAM_ID = "id";
  public static final String PARAM_PUBLIC_KEY = "public_key";

  //property that defines the type of message being sent
  public static final String MSGTYPE = "msg_name";
  public static final String MSG_REGISTER = "msg_register";
  public static final String MSG_UNREGISTER = "msg_unregister";
  public static final String MSG_MEASUREMENT = "msg_measurement";
  public static final String MSG_EVENT = "msg_event";
  public static final String MSG_ADD_MEASURE = "msg_add_measure";
  public static final String MSG_REMOVE_MEASURE = "msg_remove_measure";

  public static final String EVENT_NO_VALUE_FOR_BM = "event_no_valid_value";
  public static final String EVENT_PROBE_HANGS = "event_probe_hangs";
  public static final String MEASUREMENT_TARGET = "measurement_target";
  public static final String FILENAME = "filename";
  public static final String USERNAME = "username";
  public static final String PASSWORD = "password";
  public static final String COMMAND = "command";
  public static final String MEASURE_INTERVAL = "measure_interval";
  public static final String BROKER_ADDRESS = "broker_address";
  public static final String HTTP_URI = "http_uri";
  public static final String HTTP_PORT = "http_port";

  public static String createMeasureURI(String targetType, String targetName, String bmClass, String bmName) {
    return "MFW://"+targetType+"/"+targetName+"/"+bmClass+"/"+bmName;
  }
}

