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

package fi.vtt.noen.mfw.bundle.server.webui.derivedmeasurepage;

import osmo.common.log.Logger;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.DMDefinition;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Teemu Kanstren
 */
public class DMListDataProvider extends SortableDataProvider<DMDefinition> {
  private final static Logger log = new Logger(DMListDataProvider.class);
  private final List<DMDefinition> dms;

  public DMListDataProvider(List<DMDefinition> dms) {
    this.dms = dms;
    setSort("name", true);
  }

  public Iterator<DMDefinition> iterator(int i, int i1) {
    SortParam sp = getSort();
    String key = sp.getProperty();
    log.debug("sort key:" + key);
    if (sp.isAscending()) {
      Collections.sort(dms, new DMComparator(key, true));
    } else {
      Collections.sort(dms, new DMComparator(key, false));
    }
    return dms.subList(i, i + i1).iterator();
  }

  public int size() {
    return dms.size();
  }

  public IModel<DMDefinition> model(DMDefinition dmDescription) {
    return new DetachableDMModel(dmDescription);
  }
}
