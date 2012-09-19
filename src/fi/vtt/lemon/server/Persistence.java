/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server;

import osmo.common.log.Logger;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Persists measurement data, events, ...
 *
 * @author Teemu Kanstren
 */
public class Persistence {
  private final static Logger log = new Logger(Persistence.class);
  private Map<String, Collection<Value>> histories = new HashMap<>();

  /**
   * Reads a set of events from the database according to the given criteria.
   *
   * @param first     The index of the first item to load.
   * @param count     The number of how many items to load.
   * @param sortKey   The key according to which the results and search should be sorted.
   * @param ascending Whether the results should be sorted in ascending or descending order.
   * @return The set of Event objects matching the given criteria.
   */
/*
  public List<ServerEvent> getEvents(int first, int count, ServerEvent.SortKey sortKey, boolean ascending) {
    String sortBy = null;
    if (sortKey == ServerEvent.SortKey.MESSAGE) {
      //sort the results based on the string message describing the event
      sortBy = "ORDER BY e.message";
    } else if (sortKey == ServerEvent.SortKey.TIME) {
      //sort the results based on the time when the event was observed
      sortBy = "ORDER BY e.time";
    }
    if (sortBy == null) {
      throw new IllegalArgumentException("Unsupported sort key for event:" + sortKey);
    }
    //ASC and DESC are from the SQL spec but practically the same also in JPA
    //ASC means the results are given in ascending order (1,2,3,4,...) and DESC descending order (..., 4,3,2,1)
    String order = "asc";
    if (!ascending) {
      order = "desc";
    }
    sortBy += " " + order;
    return null;
  }
*/

  /**
   * Gives the number of events stored in the database.
   *
   * @return Number of events stored in the database.
   */
  public int getEventCount() {
    return 0;
  }

  /**
   * Reads a set of values from the database according to the given criteria.
   *
   * @param first     The index of the first item to load.
   * @param count     The number of how many items to load.
   * @param sortKey   The key according to which the results and search should be sorted.
   * @param ascending Whether the results should be sorted in ascending or descending order.
   * @return The set of Value objects matching the given criteria.
   */
  public List<Value> getValues(int first, int count, Value.SortKey sortKey, boolean ascending) {
    String sortBy = null;
    if (sortKey == Value.SortKey.PRECISION) {
      //sort the results by the precision of the stored value
      sortBy = "ORDER BY v.precision";
    } else if (sortKey == Value.SortKey.VALUE) {
      //sort the results by the string value of the stored value
      sortBy = "ORDER BY v.value";
    } else if (sortKey == Value.SortKey.MEASUREURI) {
      //sort the results by the measureURI of the value, that identifies which measure it is
      sortBy = "ORDER BY v.bm.target.targetType, v.bm.target.targetName, v.bm.bmClass, v.bm.bmName";
    } else if (sortKey == Value.SortKey.TIME) {
      //sort the results by the time when the measurement was stored
      sortBy = "ORDER BY v.time";
    }
    if (sortBy == null) {
      throw new IllegalArgumentException("Unsupported sort key for value:" + sortKey);
    }
    //sort in ascending or descenging order, same as for events above
    String order = "asc";
    if (!ascending) {
      order = "desc";
    }
    sortBy += " " + order;
    return null;
  }

  /**
   * Reads a set of values from the database according to the given criteria.
   *
   * @param sortKey   The key according to which the results and search should be sorted.
   * @param ascending Whether the results should be sorted in ascending or descending order.
   * @return The set of Value objects matching the given criteria.
   */
  public List<Value> getValues(long startTime, long endTime, Long[] bmIds, Value.SortKey sortKey, boolean ascending) {
    String sortBy = null;
    if (sortKey == Value.SortKey.PRECISION) {
      //sort the results by the precision of the stored value
      sortBy = "ORDER BY v.precision";
    } else if (sortKey == Value.SortKey.VALUE) {
      //sort the results by the string value of the stored value
      sortBy = "ORDER BY v.value";
    } else if (sortKey == Value.SortKey.MEASUREURI) {
      //sort the results by the measureURI of the value, that identifies which measure it is
      sortBy = "ORDER BY v.bm.target.targetType, v.bm.target.targetName, v.bm.bmClass, v.bm.bmName";
    } else if (sortKey == Value.SortKey.TIME) {
      //sort the results by the time when the measurement was stored
      sortBy = "ORDER BY v.time";
    }
    if (sortBy == null) {
      throw new IllegalArgumentException("Unsupported sort key for value:" + sortKey);
    }
    //sort in ascending or descenging order, same as for events above
    String order = "asc";
    if (!ascending) {
      order = "desc";
    }
    
    StringBuilder bms = new StringBuilder();
    
    for ( int i = 0; i < bmIds.length; i++ )
    {
        bms.append( "v.bm.bmId=" ).append( bmIds[ i ] );
        if ( i < bmIds.length - 1 )
        {
            bms.append( " or " );
        }
    }
    
    sortBy += " " + order;
    
    Date sd = new Date( startTime );
    Date se = new Date( endTime );
    
    SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
    
    String where = "where v.time between '" + sdf.format( sd ) +"' and '" + sdf.format( se ) + "'";
    where += " and (" + bms.toString() + ")";
    
    return null;
  }

  /**
   * Gives the number of values stored in the database.
   *
   * @return Number of values stored in the database.
   */
  public int getValueCount() {
    return 0;
  }

  public synchronized void store(Value value) {
    String measureURI = value.getMeasureURI();
    Collection<Value> history = histories.get(measureURI);
    if (history == null) {
      history = new ArrayList<>();
      histories.put(measureURI, history);
    }
    history.add(value);
  }
}