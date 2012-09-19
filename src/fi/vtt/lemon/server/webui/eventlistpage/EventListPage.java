/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.eventlistpage;

import fi.vtt.lemon.server.Persistence;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DefaultDataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.PropertyColumn;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import org.apache.wicket.model.Model;
import osmo.common.log.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * A page showing the list of events in the MFW.
 *
 * @author Teemu Kanstren
 */
public class EventListPage extends WebPage {
  private final static Logger log = new Logger(EventListPage.class);
  private Persistence persistence;

  public EventListPage() {
    add(new StyleSheetReference("listpageCSS", getClass(), "style.css"));

    createEventList();
  }

  //the persistenceplugin is used to access the event history from the DB
  public void setPersistence(Persistence persistence) {
    this.persistence = persistence;
  }

  public void createEventList() {
    List<IColumn<?>> columns = new ArrayList<IColumn<?>>();

    //first parameter = displayed name, second = sort key passed to dataprovider, third = key used to retrieve table values from data objects
    columns.add(new PropertyColumn(new Model<String>("timeString"), ServerEvent.SortKey.TIME.toString(), "timeString"));
    columns.add(new PropertyColumn(new Model<String>("source"), ServerEvent.SortKey.TIME.toString(), "source"));
    columns.add(new PropertyColumn(new Model<String>("message"), ServerEvent.SortKey.MESSAGE.toString(), "message"));

    add(new DefaultDataTable("table", columns, new EventHistoryDataProvider(persistence), 50));

  }

  @Override
  protected void onDetach() {
    super.onDetach();
  }
}
