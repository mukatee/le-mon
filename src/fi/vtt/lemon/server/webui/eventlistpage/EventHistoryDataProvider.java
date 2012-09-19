/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.eventlistpage;

import fi.vtt.lemon.server.Persistence;
import org.apache.wicket.extensions.markup.html.repeater.util.SortParam;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.model.IModel;
import osmo.common.log.Logger;

import java.util.Iterator;

/**
 * @author Teemu Kanstren
 */
public class EventHistoryDataProvider extends SortableDataProvider<ServerEvent> {
  private final static Logger log = new Logger(EventHistoryDataProvider.class);
  private Persistence persistence;

  public EventHistoryDataProvider(Persistence persistence) {
    this.persistence = persistence;
    setSort(ServerEvent.SortKey.TIME.toString(), true);
  }

  public Iterator<ServerEvent> iterator(int i1, int i2) {
    SortParam sp = getSort();
    String key = sp.getProperty();
    log.debug("sort key:" + key);
    return null;
//    return persistence.getEvents(i1, i2, ServerEvent.SortKey.valueOf(key), sp.isAscending()).iterator();
/*    if (sp.isAscending()) {
      Collections.sort(events, new EventComparator(key, true));
    } else {
      Collections.sort(events, new EventComparator(key, false));
    }
    return events.subList(i, i + i1).iterator();*/
  }

  public int size() {
    return persistence.getEventCount();
  }

  public IModel<ServerEvent> model(ServerEvent event) {
    return new DetachableEventModel(event);
  }
}