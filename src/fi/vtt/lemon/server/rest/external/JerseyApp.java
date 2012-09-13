package fi.vtt.lemon.server.rest.external;

import fi.vtt.lemon.server.rest.external.resources.AvailabilityJSON;
import fi.vtt.lemon.server.rest.external.resources.BaseMeasureJSON;
import fi.vtt.lemon.server.rest.external.resources.FrameworkInfoJSON;
import fi.vtt.lemon.server.rest.external.resources.HistoryJSON;
import fi.vtt.lemon.server.rest.external.resources.ProbeConfigurationJSON;
import fi.vtt.lemon.server.rest.external.resources.ProbesJSON;
import fi.vtt.lemon.server.rest.external.resources.RegisterJSON;
import fi.vtt.lemon.server.rest.external.resources.ShutdownJSON;
import fi.vtt.lemon.server.rest.external.resources.SubscriptionJSON;

import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/** @author Teemu Kanstren */
public class JerseyApp extends Application {
  @Override
  public Set<Class<?>> getClasses() {
    Set<Class<?>> resources = new HashSet<>();
    resources.add(ShutdownJSON.class);
    resources.add(RegisterJSON.class);
    resources.add(ProbesJSON.class);
    resources.add(BaseMeasureJSON.class);
    resources.add(HistoryJSON.class);
    resources.add(SubscriptionJSON.class);
    resources.add(ProbeConfigurationJSON.class);
    resources.add(FrameworkInfoJSON.class);
    resources.add(AvailabilityJSON.class);
    return resources;
  }

}