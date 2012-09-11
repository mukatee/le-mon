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

package fi.vtt.lemon.server.webui.bmreportspage;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import fi.vtt.lemon.server.shared.datamodel.BMReport;

/**
 * @author Teemu Kanstren
 */
public class ActionPanel extends Panel {
  /**
   * @param id    component id
   * @param model model for contact
   */
  public ActionPanel(final BMReportsPage page, String id, IModel<BMReport> model) {
    super(id, model);
    add(new Link("change_reference") {
      @Override
      public void onClick() {
        page.changeReference((BMReport) getParent().getDefaultModelObject());
      }
    });
  }

}