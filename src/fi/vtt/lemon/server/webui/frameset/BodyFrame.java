/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.frameset;

import fi.vtt.lemon.server.webui.historypage.HistoryPage;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageMap;
import org.apache.wicket.RequestCycle;
import org.apache.wicket.markup.html.WebComponent;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

/**
 * Second frame that splits the bottom frame into two horizontally
 *
 * @author Teemu Kanstren
 */
public class BodyFrame extends WebPage {
  /**
   * Model that returns the url to the bookmarkable page that is set in the current frame target.
   */
  private final class FrameModel implements IModel<CharSequence> {
    public CharSequence getObject() {
      return RequestCycle.get().urlFor(PageMap.forName(RIGHT_FRAME_NAME),
              frameTarget.getFrameClass(), null);
    }

    public void setObject(final CharSequence object) {
    }

    /**
     * @see org.apache.wicket.model.IDetachable#detach()
     */
    public void detach() {
    }
  }

  /**
   * name for page map etc.
   */
  public static final String RIGHT_FRAME_NAME = "right";

  private final FrameTarget frameTarget = new FrameTarget(HistoryPage.class);

  /**
   * Constructor
   */
  @SuppressWarnings("unchecked")
  public BodyFrame() {
    // create a new page instance, passing this 'master page' as an argument
    LeftFrame leftFrame = new LeftFrame(this);
    // get the url to that page
    String leftFrameSrc = RequestCycle.get().urlFor(leftFrame).toString();
    // and create a simple component that modifies it's src attribute to
    // hold the url to that frame
    WebComponent leftFrameTag = new WebComponent("leftFrame");
    leftFrameTag.add(new AttributeModifier("src", new Model<String>(leftFrameSrc)));
    add(leftFrameTag);

    // make a simple component for the right frame tag
    WebComponent rightFrameTag = new WebComponent("rightFrame");
    // and this time, set a model which retrieves the url to the currently
    // set frame class in the frame target
    rightFrameTag.add(new AttributeModifier("src", new FrameModel()));
    add(rightFrameTag);
  }

  /**
   * Gets frameTarget.
   *
   * @return frameTarget
   */
  public FrameTarget getFrameTarget() {
    return frameTarget;
  }

  /**
   * @see org.apache.wicket.Component#isVersioned()
   */
  @Override
  public boolean isVersioned() {
    return false;
  }
}
