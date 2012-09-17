package fi.vtt.lemon.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import fi.vtt.lemon.server.LemonServer;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.core.MediaType;

import static fi.vtt.lemon.server.external.RESTConst.*;

/** @author Teemu Kanstren */
public class History {
  public static void main(String[] args) throws Exception {
    LemonServer.main(null);

    Client client = Client.create();
    WebResource wr = client.resource("http://localhost:11111/rest/");
    WebResource.Builder path = wr.path(PATH_HISTORY).accept(MediaType.APPLICATION_JSON).type(MediaType.APPLICATION_JSON);
    JSONObject json = new JSONObject();
    json.put(MEASURE_URI, "huuh");
    ClientResponse data = path.post(ClientResponse.class, json.toString());
    
    System.out.println("data:"+data);
  }
}
