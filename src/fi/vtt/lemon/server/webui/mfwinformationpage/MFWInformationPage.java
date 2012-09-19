/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.mfwinformationpage;

import fi.vtt.lemon.server.webui.WebUIPlugin;
import fi.vtt.lemon.server.webui.mfwclient.MFW;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.resources.StyleSheetReference;
import osmo.common.log.Logger;


public class MFWInformationPage extends WebPage {
  private final static Logger log = new Logger(MFWInformationPage.class);
  private WebUIPlugin webUI;

  public MFWInformationPage() {
    webUI = WebUIPlugin.getInstance();
    add(new StyleSheetReference("listpageCSS", getClass(), "style.css"));
    
    MFW mfwInfo = webUI.getMFWInformation();
    
    Label mfwIdLabel = new Label("mfwid", "ID: " +Long.toString(mfwInfo.getId()));
    add(mfwIdLabel);
    
    Label versionLabel = new Label("version", "Version: " +mfwInfo.getVersion());
    add(versionLabel);
    
    Label ifsLabel = new Label("ifs", "IFs: " +mfwInfo.getIfs());
    add(ifsLabel);
    
    Label companyLabel = new Label("company", "Company: " +mfwInfo.getCompany());
    add(companyLabel);
    
    Label timeLabel = new Label("time", "Time: " +mfwInfo.getTime().toString());
    add(timeLabel);
    
  }

}
