package fi.vtt.lemon.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.probes.tester.TestProbe;
import fi.vtt.lemon.probes.tester.TestProbe1;
import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.external.JerseyApp;
import fi.vtt.lemon.server.external.RESTConst;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.ws.rs.core.MediaType;

/** @author Teemu Kanstren */
public class Availability {
  public static void main(String[] args) throws Exception {
//    Logger.debug = true;
    LemonServer.main(null);

    TestProbe probe = new TestProbe1();
    probe.start();

    Client client = Client.create();
    WebResource wr = client.resource("http://localhost:11111/rest/");
    Thread.sleep(5555);
    String data = wr.path(RESTConst.PATH_AVAILABILITY).accept(MediaType.APPLICATION_JSON).get(String.class);
    JSONObject json = new JSONObject(data);
    JSONArray array = json.getJSONArray("availability");
    JSONObject item = array.getJSONObject(0);
    System.out.println("json:"+item.get(RabbitConst.PARAM_MEASURE_URI));
  }
}
