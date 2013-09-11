package fi.vtt.lemon.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.probes.tester.ConfigurableTestProbe;
import fi.vtt.lemon.probes.tester.TestProbe;
import fi.vtt.lemon.probes.tester.TestProbe1;
import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.external.RESTConst;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import osmo.common.log.Logger;

import javax.ws.rs.core.MediaType;

import static fi.vtt.lemon.server.external.RESTConst.*;
import static fi.vtt.lemon.RabbitConst.*;
import static org.junit.Assert.assertEquals;

/** @author Teemu Kanstren */
public class AddRemoveMeasure {
  @Test
  public void addRemoveMeasure() throws Exception {
    //Logger.debug = true;
    LemonServer.main(null);

    TestProbe probe = new ConfigurableTestProbe();
    probe.start();

    Client client = Client.create();
    WebResource wr = client.resource("http://localhost:11111/rest/");
    Thread.sleep(2222);

    WebResource.Builder addPath = wr.path(PATH_ADD_MEASURE).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
    JSONObject json = new JSONObject();
    json.put(MEASURE_URI, "le-mon://configurable/test-probe");
    json.put(PARAM_CONFIG, "my little measure");
    ClientResponse data = addPath.post(ClientResponse.class, json.toString());

    System.out.println("set to my little measure");

    Thread.sleep(2222);

    WebResource.Builder removePath = wr.path(PATH_REMOVE_MEASURE).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
    json = new JSONObject();
    json.put(MEASURE_URI, "le-mon://configurable/test-probe");
    json.put(PARAM_CONFIG, "hello world");
    data = removePath.post(ClientResponse.class, json.toString());

    System.out.println("attempted to remove non-existent");

    Thread.sleep(2222);

    removePath = wr.path(PATH_REMOVE_MEASURE).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
    json = new JSONObject();
    json.put(MEASURE_URI, "le-mon://configurable/test-probe");
    json.put(PARAM_CONFIG, "my little measure");
    data = removePath.post(ClientResponse.class, json.toString());

    System.out.println("attempted to remove existent");

    Thread.sleep(2222);

    //TODO: finish this
  }
}
