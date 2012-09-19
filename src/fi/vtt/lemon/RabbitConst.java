/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon;

/**
 * Contains shared constant values for the different MFW implementation elements.
 *
 * @author Teemu Kanstren
 */
public class RabbitConst {
  //server-agent xmlrpc address in probe-agent configuration
  public static final String SERVER_URL = "server_agent_url";
  public static final String MEASURE_INTERVAL = "measure_interval";
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
  //configuration key for ssh probe script file name
  public static final String SSH_SCRIPT_FILENAME = "ssh_script_file";
  //configuration key for ssh probe script file contents
  public static final String SSH_SCRIPT_FILE_CONTENTS = "ssh_script_file_contents";
  //configuration key for ssh probe user name to do the login on the target
  public static final String SSH_USERNAME = "ssh_username";
  //configuration key for ssh probe password to do the login on the target
  public static final String SSH_PASSWORD = "ssh_password";
  //configuration key for ssh probe giving the shell command to execute the given script
  public static final String SSH_SCRIPT_COMMAND = "ssh_command";
  //prefix for test probe-agent configuration file
  public static final String TEST_PROBE_AGENT_CONFIG_PREFIX = "test";
  //name of the filename from which all the configuration of agents is always read
  public static final String CONFIGURATION_FILENAME = "noen-mfw.properties";
  public static final int ERROR_CODE_ILLEGAL_ARGUMENTS_FOR_PROBE = -1;
  //maximum time (in milliseconds) for not receiving a keep-alive message from a probe before "disabling" that probe
  public static final String MAX_KEEPALIVE_DELAY = "max_keepalive_delay";
  //time to wait between trying to reconnect to server
  public static final String RETRY_DELAY = "retry_delay";
  public static final String REST_CLIENT_ENDPOINT_URL = "rest_client_endpoint";
  public static final String THREAD_POOL_SIZE = "thread_pool_size";
  public static final String TASK_TIMEOUT = "task_timeout";

  public static final String PARAM_TIME = "time";
  public static final String PARAM_MEASURE_URI = "measure_uri";
  public static final String PARAM_PRECISION = "precision";
  public static final String PARAM_VALUE = "value";
  public static final String PARAM_EVENT_TYPE = "event_type";
  public static final String PARAM_EVENT_SOURCE = "event_source";
  public static final String PARAM_EVENT_MSG = "event_msg";
  public static final String PARAM_PUBLIC_KEY = "public_key";

  public static final String SERVER_QUEUE = "le-mon_server_queue";

  //property that defines the type of message being sent
  public static final String MSGTYPE = "msg_name";
  public static final String MSG_MEASUREMENT = "msg_measurement";
  public static final String MSG_EVENT = "msg_event";

  public static final String EVENT_NO_VALUE_FOR_BM = "event_no_valid_value";
  public static final String EVENT_PROBE_HANGS = "event_probe_hangs";

  public static String createMeasureURI(String targetType, String targetName, String bmClass, String bmName) {
    return "MFW://"+targetType+"/"+targetName+"/"+bmClass+"/"+bmName;
  }
}

