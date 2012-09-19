/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.external;


import fi.vtt.lemon.RabbitConst;
import fi.vtt.lemon.server.Value;
import osmo.common.log.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * @author Teemu Kanstren
 */
public class RestClient {
  private final static Logger log = new Logger(RestClient.class);

  public RestClient() {
    String url = null;
    try {
      InputStream in = new FileInputStream(RabbitConst.CONFIGURATION_FILENAME);
      Properties props = new Properties();
      props.load(in);
      url = props.getProperty(RabbitConst.REST_CLIENT_ENDPOINT_URL);
      if (url == null) {
        throw new IllegalArgumentException("No URL defined for REST client endpoint. Unable to start REST plugin.");
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to read configuration file '" + RabbitConst.CONFIGURATION_FILENAME + "'", e);
    }
    log.debug("Initializing REST plugin with endpoint "+url);
    log.debug("REST plugin initialized");
  }

  public void bmResult(Value value) {
    //BMValue bm = new BMValue();
    //bm.setValue(value.getString());
    //bm.setTime(value.getTime());
    log.debug("Sending BM results via REST interface");
//    wr.path("bmresult/" + bmId).type(MediaType.APPLICATION_XML).post(bm);
  }
}

