package fi.vtt.lemon.rest;

import fi.vtt.lemon.Config;
import fi.vtt.lemon.server.rest.RestClient;
import org.codehaus.jettison.json.JSONObject;

import static fi.vtt.lemon.server.rest.RESTConst.*;

/**
 * @author Teemu Kanstren
 */
public class ManualTester {
  public static void main(String[] args) throws Exception {
    new FakeJettyStarter().start();
    JSONObject measureReq = new JSONObject();
    measureReq.put(MEASURE_URI, "le-mon://my_test_probe");
    int port = Config.getInt(REST_SERVER_SERVER_PORT, 11112);
    String url = "http://localhost:"+port;
    RestClient rs2 = new RestClient(url);
    rs2.post(PATH_SUBSCRIBE, measureReq);
  }
}
