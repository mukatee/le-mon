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

package fi.vtt.lemon.server.webui.derivedmeasurepage;

import osmo.common.log.Logger;
import fi.vtt.lemon.server.shared.datamodel.DMDefinition;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextArea;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;

/**
 * @author Teemu Kanstren
 */
public class ScriptForm extends Form<ValueMap> {
  private final static Logger log = new Logger(ScriptForm.class);
  private final ValueMap properties = new ValueMap();
  private DMDefinition dm = null;
  private TextArea textArea;

  public ScriptForm(final String id) {
    super(id);
    // this is just to make the unit test happy
    setMarkupId("scriptForm");
    // Attach textfield components that edit properties map model
    textArea = new TextArea("script", new PropertyModel<String>(properties, "script"));
    add(textArea);
  }

  /**
   * Show the resulting valid edit
   */
  @Override
  public final void onSubmit() {
    String script = properties.getString("script");
    log.debug("Script:"+script);
    dm.setScript(script);
  }

  public void setDM(DMDefinition selected) {
    this.dm = selected;
    log.debug("Set DM:"+selected);
    properties.put("script", dm.getScript());
  }
}
