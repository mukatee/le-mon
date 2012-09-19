/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.bmlistpage;

import fi.vtt.lemon.server.Registry;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import osmo.common.log.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author Teemu Kanstr�n
 */
public class BMListDataProvider extends SortableDataProvider<BMListItem> {
  private final static Logger log = new Logger(BMListDataProvider.class);
  private final Registry registry;
  private final Map<String, String> latestValues;

  public BMListDataProvider(Registry registry, Map<String, String> latestValues) {
    this.registry = registry;
    this.latestValues = latestValues;
    setSort("targetId", true);
  }

  public Iterator<BMListItem> iterator(int i, int i1) {
    List<String> bms = registry.getAvailableBM();
    bms = bms.subList(i, i + i1);
    List<BMListItem> list = new ArrayList<BMListItem>();
    for (String bm : bms) {
      String value = latestValues.get(bm);
      BMListItem listItem = new BMListItem(bm, value);
      list.add(listItem);
    }

    SortParam sp = getSort();
    String key = sp.getProperty();
    log.debug("sort key:" + key);
    if (sp.isAscending()) {
      Collections.sort(list, new BMComparator(key, true));
    } else {
      Collections.sort(list, new BMComparator(key, false));
    }
    return list.iterator();
  }

  public int size() {
    return registry.getAvailableBM().size();
  }

  public IModel<BMListItem> model(BMListItem bm) {
    return new DetachableBMModel(bm);
  }
}
