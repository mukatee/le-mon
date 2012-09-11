/*
 * Copyright (C) 2010-2011 VTT Technical Research Centre of Finland.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package fi.vtt.lemon.server.rest.external;


import fi.vtt.lemon.common.Const;
import fi.vtt.lemon.server.rest.external.resources.BMValue;
import fi.vtt.lemon.server.shared.datamodel.Value;
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
      InputStream in = new FileInputStream(Const.CONFIGURATION_FILENAME);
      Properties props = new Properties();
      props.load(in);
      url = props.getProperty(Const.REST_CLIENT_ENDPOINT_URL);
      if (url == null) {
        throw new IllegalArgumentException("No URL defined for REST client endpoint. Unable to start REST plugin.");
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to read configuration file '" + Const.CONFIGURATION_FILENAME + "'", e);
    }
    log.debug("Initializing REST plugin with endpoint "+url);
    log.debug("REST plugin initialized");
  }

  public void bmResult(Value value) {
    BMValue bm = new BMValue();
    bm.setValue(value.getString());
    bm.setTime(value.getTime());
    log.debug("Sending BM results via REST interface");
    long bmId = value.getBm().getBmId();
//    wr.path("bmresult/" + bmId).type(MediaType.APPLICATION_XML).post(bm);
  }
}

