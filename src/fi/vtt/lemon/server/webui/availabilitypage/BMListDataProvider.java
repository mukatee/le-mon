/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.availabilitypage;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import osmo.common.log.Logger;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class BMListDataProvider extends SortableDataProvider<BMDesc> {
  private final static Logger log = new Logger(BMListDataProvider.class);
  private final List<BMDesc> bms;

  public BMListDataProvider(List<BMDesc> bms) {
    this.bms = bms;
    setSort("name", true);
  }

  public Iterator<BMDesc> iterator(int i, int i1) {
    SortParam sp = getSort();
    String key = sp.getProperty();
    log.debug("sort key:" + key);
    if (sp.isAscending()) {
      Collections.sort(bms, new BMComparator(key, true));
    } else {
      Collections.sort(bms, new BMComparator(key, false));
    }
    return bms.subList(i, i + i1).iterator();
  }

  public int size() {
    return bms.size();
  }

  public IModel<BMDesc> model(BMDesc bm) {
    return new DetachableBMModel(bm);
  }
}
