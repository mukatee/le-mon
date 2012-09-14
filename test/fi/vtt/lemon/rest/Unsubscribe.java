package fi.vtt.lemon.rest;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import fi.vtt.lemon.server.external.JerseyApp;
import fi.vtt.lemon.server.external.RESTConst;

import javax.ws.rs.core.MediaType;

/** @author Teemu Kanstren */
public class Unsubscribe {
  public static void main(String[] args) throws Exception {
    JerseyApp jersey = new JerseyApp();
    jersey.start();

    Client client = Client.create();
    WebResource wr = client.resource("http://localhost:11111/rest/");
    String data = wr.path(RESTConst.PATH_FRAMEWORK_INFO).accept(MediaType.APPLICATION_JSON).get(String.class);
    System.out.println("data:"+data);
  }
}
