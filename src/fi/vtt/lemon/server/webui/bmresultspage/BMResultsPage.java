/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.bmresultspage;

import fi.vtt.lemon.server.webui.WebUIPlugin;
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
 * @author Teemu Kanstren
 */
public class BMResultsPage extends WebPage {
  private final static Logger log = new Logger(BMResultsPage.class);
  private WebUIPlugin webUi;

  public BMResultsPage() throws Exception {
    webUi = WebUIPlugin.getInstance();
    add(new StyleSheetReference("listpageCSS", getClass(), "style.css"));
    createBMResultsList();
  }

  public void createBMResultsList() {
    List<BMResult> bmResults = readBMResults();
    List<IColumn<?>> columns = new ArrayList<IColumn<?>>();

    columns.add(new PropertyColumn(new Model<String>("time"), "time", "time"));
    columns.add(new PropertyColumn(new Model<String>("bm_id"), "bm_id", "bm_id") {
      @Override
      public String getCssClass() {
        return "numeric";
      }
    });
    columns.add(new PropertyColumn(new Model<String>("device_id"), "device_id", "device_id") {
      @Override
      public String getCssClass() {
        return "numeric";
      }
    });
    columns.add(new PropertyColumn(new Model<String>("value"), "value", "value"));
    columns.add(new PropertyColumn(new Model<String>("error"), "error", "error"));

    add(new DefaultDataTable("table", columns, new BMResultDataProvider(bmResults), 20));
  }

  private List<BMResult> readBMResults() {
    return webUi.getBMResults();
  }

}