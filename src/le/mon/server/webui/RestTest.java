package le.mon.server.webui;

import le.mon.server.rest.RESTConst;
import le.mon.server.rest.RestClient;
import org.codehaus.jettison.json.JSONObject;

/**
 * @author Teemu Kanstren
 */
public class RestTest {
  public static void main(String[] args) throws Exception {
    JSONObject data = new JSONObject();
    data.put("my_msg", "hello msg");
    RestClient.sendPost("http://localhost:11111" + RESTConst.PATH_AVAILABILITY, data);
  }
}
