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


public class UnsubscribeToBMForm extends Form<ValueMap> {
  private final static Logger log = new Logger(UnsubscribeToBMForm.class);
  private final ValueMap properties = new ValueMap();

  public UnsubscribeToBMForm(String id) {
    super(id);
    add(new TextArea("bmid", new PropertyModel<String>(properties, "bmid")));
    add(new TextArea("deviceid", new PropertyModel<String>(properties, "deviceid")));
  }

  @Override
  protected void onSubmit() {
    String bmId = properties.getString("bmid");
    String deviceId = properties.getString("deviceid");
    try {
      log.debug("bmId:"+bmId);
      log.debug("deviceId:"+deviceId);
      WebUIPlugin webUI = WebUIPlugin.getInstance();
      webUI.unsubscribeToBM(Long.parseLong(bmId), Long.parseLong(deviceId));
      
    } catch (Exception e) {
      //log.debug("Error   ");
    }
    
  }
}
