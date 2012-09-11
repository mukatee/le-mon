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


import fi.vtt.lemon.common.DataType;
import fi.vtt.lemon.probe.shared.ProbeEvent;
import fi.vtt.lemon.server.rest.external.resources.Event;
import fi.vtt.lemon.server.shared.datamodel.ProbeDisabled;
import fi.vtt.lemon.server.shared.datamodel.ProbeRegistered;
import fi.vtt.lemon.server.shared.datamodel.Value;
import osmo.common.log.Logger;

import java.util.ArrayList;


/**
 * @author Petri Heinonen
 */
public class RestClientEndpoint {
  private final static Logger log = new Logger(RestClientEndpoint.class);

  public RestClientEndpoint( String url ) {
    log.debug("Initializing REST plugin with endpoint "+url);
    log.debug("REST plugin initialized");
  }

  public void measurement(Value value) {
    log.debug("Sending BM results via REST interface");
    // TODO Need to handle multiple measurement values
    // There should be a queue for measurements that comes during
    // measurement result sending time interval
  }
  
  public void probeEvent( ProbeEvent pe )
  {
    Event e = new Event( DataType.PROBE_EVENT.name(), "Probe event occurred." );
//    wr.path( "client/event" ).type( MediaType.APPLICATION_XML ).post( e );
  }
  
  public void probeRegistered( ProbeRegistered pr )
  {
    Event e = new Event( DataType.PROBE_REGISTERED.name(), "Probe registered." );
//    wr.path( "client/event" ).type( MediaType.APPLICATION_XML ).post( e );
  }
  
  public void probeDisabled( ProbeDisabled pd )
  {
    Event e = new Event( DataType.PROBE_DISABLED.name(), "Probe disabled." );
//    wr.path( "client/event" ).type( MediaType.APPLICATION_XML ).post( e );
  }
}