/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.historypage;

import fi.vtt.lemon.server.Persistence;
import fi.vtt.lemon.server.Value;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import osmo.common.log.Logger;

import java.util.Iterator;
import java.util.List;

/**
 * @author Teemu Kanstren
 */
public class MeasurementHistoryDataProvider extends SortableDataProvider<Value> {
  private transient final static Logger log = new Logger(MeasurementHistoryDataProvider.class);
  private transient Persistence persistence;

  public MeasurementHistoryDataProvider(Persistence persistence) {
    this.persistence = persistence;
    setSort(Value.SortKey.MEASUREURI.toString(), true);
  }

  public Iterator<Value> iterator(int first, int count) {
    SortParam sp = getSort();
    String key = sp.getProperty();
    log.debug("sort key:" + key);
    List<Value> values = persistence.getValues(first, count, Value.SortKey.valueOf(key), sp.isAscending());
    //log.debug("values:"+values);
    return values.iterator();
/*    if (sp.isAscending()) {
      Collections.sort(measures, new ValueComparator(key, true));
    } else {
      Collections.sort(measures, new ValueComparator(key, false));
    }
    return measures.subList(i, i + i1).iterator();*/
  }

  public int size() {
    int valueCount = persistence.getValueCount();
    log.debug("valuecount:"+valueCount);
    return valueCount;
  }

  public IModel<Value> model(Value measure) {
    return new DetachableValueModel(measure);
  }
}