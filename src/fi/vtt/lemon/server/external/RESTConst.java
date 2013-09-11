/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.external;

/** 
 * Defines a set of constants for the system.
 * 
 * @author Teemu Kanstren 
 */
public class RESTConst {
  public static final String AVAILABILITY_ARRAY = "availability";
  public static final String MEASURE_URI = "measure_uri";
  public static final String TIME = "time";
  public static final String VALUE = "value";
  public static final String START_TIME = "start_time";
  public static final String END_TIME = "end_time";
  public static final String BM_LIST = "bm_list";
  public static final String INFO = "info";
  public static final String PRECISION = "precision";

  public static final String PATH_AVAILABILITY = "/availability";
  public static final String PATH_HISTORY = "/history";
  public static final String PATH_FRAMEWORK_INFO = "/info";
  public static final String PATH_SUBSCRIBE = "/subscribe/";
  public static final String PATH_UNSUBSCRIBE = "/unsubscribe/";
  public static final String PATH_SHUTDOWN = "/shutdown";
  public static final String PATH_ADD_MEASURE = "/add_measure";
  public static final String PATH_REMOVE_MEASURE = "/remove_measure";

  public static final String PATH_BM_RESULT = "/value";
  public static final String CLIENT_URL = "client_url";
  public static final String REST_SERVER_PORT = "rest_server_port";
}
