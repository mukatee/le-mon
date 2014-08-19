package fi.vtt.lemon.server.webui.jettytest;

import org.apache.wicket.Component;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * @author Teemu Kanstren
 */
public class WicketTestHomePage extends WebPage {
  public WicketTestHomePage() {
    add(new Label("helloMsg", "hello from wicket"));
  }
}
