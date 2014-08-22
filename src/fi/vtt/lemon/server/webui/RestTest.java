package fi.vtt.lemon.server.webui;

import fi.vtt.lemon.server.external.RESTConst;
import fi.vtt.lemon.server.external.RestClient2;
import org.codehaus.jettison.json.JSONObject;

/**
 * @author Teemu Kanstren
 */
public class RestTest {
  public static void main(String[] args) throws Exception {
    JSONObject data = new JSONObject();
    data.put("my_msg", "hello msg");
    RestClient2.sendPost("http://localhost:11111"+ RESTConst.PATH_AVAILABILITY, data);
  }
}
