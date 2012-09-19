/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.subscribetobmpage;

import fi.vtt.lemon.server.webui.WebUIPlugin;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;
import osmo.common.log.Logger;

public class SubscribeToBMForm extends Form<ValueMap> {
  private final static Logger log = new Logger(SubscribeToBMForm.class);
  private final ValueMap properties = new ValueMap();

  public SubscribeToBMForm(String id) {
    super(id);
    add(new TextArea("bmid", new PropertyModel<String>(properties, "bmid")));
    add(new TextArea("deviceid", new PropertyModel<String>(properties, "deviceid")));
    add(new TextArea("frequency", new PropertyModel<String>(properties, "frequency")));
  }

  @Override
  protected void onSubmit() {
    String bmId = properties.getString("bmid");
    String deviceId = properties.getString("deviceid");
    String frequency = properties.getString("frequency");
    try {
      log.debug("bmId:"+bmId);
      log.debug("deviceId:"+deviceId);
      log.debug("frequency:"+frequency);
      WebUIPlugin webUI = WebUIPlugin.getInstance();
      webUI.subscribeToBM(Long.parseLong(bmId), Long.parseLong(deviceId), Long.parseLong(frequency));
      
    } catch (Exception e) {
      //log.debug("Error   ");
    }
    
  }
}
