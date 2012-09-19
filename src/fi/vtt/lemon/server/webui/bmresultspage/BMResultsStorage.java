/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.webui.bmresultspage;

//import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class BMResultsStorage {
  private final Vector<BMResult> bmResults = new Vector<BMResult>(100);
  private final int maxLimit = 1000;
  
  public synchronized void addBMResult(BMResult bmResult) {
    // limit the maximum size of bm results by removing the oldest one
    if (bmResults.size() >= maxLimit) {
      bmResults.remove(0);
    }
    bmResults.add(bmResult);
  }

  public synchronized List<BMResult> getBmResults() {
    return bmResults;
  }
  
}
