/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.historypage;

import fi.vtt.lemon.server.Persistence;
import fi.vtt.lemon.server.Value;
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
 * Provides a history of measurement values for the MFW. A page with a table of the values.
 *
 * @author Teemu Kanstren
 */
public class HistoryPage extends WebPage {
  private transient final static Logger log = new Logger(HistoryPage.class);
  private transient Persistence persistence;

  public HistoryPage() {
    add(new StyleSheetReference("listpageCSS", getClass(), "style.css"));
    createHistoryList();
  }

  public void setPersistence(Persistence persistence) {
    this.persistence = persistence;
  }

  public void createHistoryList() {
    List<IColumn<?>> columns = new ArrayList<IColumn<?>>();

    //first parameter = displayed name, second = sort key passed to dataprovider, third = key used to retrieve table values from data objects
    columns.add(new PropertyColumn(new Model<String>("time"), Value.SortKey.TIME.toString(), "time"));
    columns.add(new PropertyColumn(new Model<String>("measureURI"), Value.SortKey.MEASUREURI.toString(), "measureURI"));
    columns.add(new PropertyColumn(new Model<String>("value"), Value.SortKey.VALUE.toString(), "value"));
    columns.add(new PropertyColumn(new Model<String>("precision"), Value.SortKey.PRECISION.toString(), "precision") {
      @Override
      public String getCssClass() {
        return "numeric";
      }
    });

    add(new DefaultDataTable("table", columns, new MeasurementHistoryDataProvider(persistence), 50));
  }

  @Override
  protected void onDetach() {
    super.onDetach();
  }
}