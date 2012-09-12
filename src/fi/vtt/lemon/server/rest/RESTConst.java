package fi.vtt.lemon.server.rest;

/** @author Teemu Kanstren */
public class RESTConst {
  public static final String PROBE_ID = "probe_id";
  public static final String BM_ID = "bm_id";
  public static final String BM_CLASS = "bm_class";
  public static final String BM_NAME = "bm_name";
  public static final String BM_DESC = "bm_desc";
  public static final String TIME = "time";
  public static final String VALUE = "value";
  public static final String START_TIME = "start_time";
  public static final String END_TIME = "end_time";
  public static final String BM_LIST = "bm_list";
  public static final String NAME = "name";
  public static final String VALUES = "values";
  public static final String ENDPOINT = "endpoint";
  public static final String INFO = "info";
  public static final String INTERVAL = "interval";
  public static final String TARGET_ID = "tareget_id";
  public static final String TYPE = "type";
  public static final String DATA_TYPE = "data_type";
  public static final String PROBE_LIST = "probe_list";
  public static final String TARGET_LIST = "target_list";

  public static final String PATH_AVAILABILITY = "/availability";
  public static final String PATH_HISTORY = "/history";
  public static final String PATH_CONFIGURATION = "/configuration/{"+PROBE_ID+"}";
  public static final String PATH_REGISTER = "/register";
  public static final String PATH_BASE_MEASURE = "/bm/{"+BM_ID+"}";
  public static final String PATH_FRAMEWORK_INFO = "/info";
  public static final String PATH_SUBSCRIPTION = "/subcription/{"+BM_ID+"}";
  public static final String PATH_SHUTDOWN = "/shutdown";
  public static final String PATH_PROBE_INFO = "/probe_info";
}
