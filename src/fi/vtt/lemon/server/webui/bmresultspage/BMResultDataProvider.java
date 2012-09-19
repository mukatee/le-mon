/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.bmresultspage;

import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import osmo.common.log.Logger;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Teemu Kanstren
 */
public class BMResultDataProvider extends SortableDataProvider<BMResult> {
  private final static Logger log = new Logger(BMResultDataProvider.class);

  private final List<BMResult> bmResults;

  public BMResultDataProvider(List<BMResult> bmResults) {
    this.bmResults = bmResults;
    setSort("bm_id", true);
  }

  public Iterator<BMResult> iterator(int i, int i1) {
    SortParam sp = getSort();
    String key = sp.getProperty();
    log.debug("sort key:" + key);
    if (sp.isAscending()) {
      Collections.sort(bmResults, new ValueComparator(key, true));
    } else {
      Collections.sort(bmResults, new ValueComparator(key, false));
    }
    return bmResults.subList(i, i + i1).iterator();
  }

  public int size() {
    return bmResults.size();
  }

  public IModel<BMResult> model(BMResult bmResult) {
    return new DetachableBMResultModel(bmResult);
  }
}