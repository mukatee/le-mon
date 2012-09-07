/*
 * Copyright (C) 2010-2011 VTT Technical Research Centre of Finland.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package fi.vtt.noen.mfw.bundle.server;

import fi.vtt.noen.mfw.bundle.common.Const;
import fi.vtt.noen.mfw.bundle.common.DataObject;
import fi.vtt.noen.mfw.bundle.common.KnowledgeSource;
import osmo.common.log.Logger;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.BMDescription;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.ServerEvent;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.ProbeDescription;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.TargetDescription;
import fi.vtt.noen.mfw.bundle.server.shared.datamodel.Value;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Persists measurement data, events, derivedmeasure, etc. Basically anything from the blackboard.
 *
 * @author Teemu Kanstren
 * @see KnowledgeSource
 */
public class PersistencePlugin {
  private final static Logger log = new Logger(PersistencePlugin.class);
  private boolean stopped = false;

  public PersistencePlugin() {
  }

  /**
   * Called from BlackBoard when data this plugin subscribes to is available.
   *
   * @param data The new data that is available for processing.
   */
  public void process(DataObject data) {
    if (stopped) throw new IllegalStateException("Attempting to use a stopped persistence plugin.");
  }

  /**
   * Called by the PersistenceBundleActivator when the bundle is shutting down.
   */
  public void stop() {
    log.debug("Persistence plugin stop");
    stopped = true;
  }

  /**
   * Defines what the blackboard gives us (process()).
   *
   * @return The subscribed data types.
   */
  public Set<Class> getCommands() {
    return createCommandSet(Value.class, ServerEvent.class);
  }

  /**
   * Reads a set of events from the database according to the given criteria.
   *
   * @param first     The index of the first item to load.
   * @param count     The number of how many items to load.
   * @param sortKey   The key according to which the results and search should be sorted.
   * @param ascending Whether the results should be sorted in ascending or descending order.
   * @return The set of Event objects matching the given criteria.
   */
  public List<ServerEvent> getEvents(int first, int count, ServerEvent.SortKey sortKey, boolean ascending) {
    if (stopped) throw new IllegalStateException("Attempting to use a stopped persistence plugin.");
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
    return result;
  }

  /**
   * Gives the number of events stored in the database.
   *
   * @return Number of events stored in the database.
   */
  public int getEventCount() {
    if (stopped) throw new IllegalStateException("Attempting to use a stopped persistence plugin.");
    return result.intValue();
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
    if (stopped) throw new IllegalStateException("Attempting to use a stopped persistence plugin.");
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
    return result;
  }

  /**
   * Reads a set of values from the database according to the given criteria.
   *
   * @param sortKey   The key according to which the results and search should be sorted.
   * @param ascending Whether the results should be sorted in ascending or descending order.
   * @return The set of Value objects matching the given criteria.
   */
  public List<Value> getValues(long startTime, long endTime, Long[] bmIds, Value.SortKey sortKey, boolean ascending) {
    if (stopped) throw new IllegalStateException("Attempting to use a stopped persistence plugin.");
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
    
    return result;
  }

  /**
   * Gives the number of values stored in the database.
   *
   * @return Number of values stored in the database.
   */
  public int getValueCount() {
    if (stopped) throw new IllegalStateException("Attempting to use a stopped persistence plugin.");
    return result.intValue();
  }

  /**
   * Creates a ProbeDescription for the given information. Checks the DB for an existing suitable description.
   * If none is found, a new one is created and stored into the database. Whichever succeeds, the result is returned.
   *
   * @param properties The information describing the probe.
   * @return The ProbeDescription object matching the given information.
   */
  public ProbeDescription createProbeDescription(Map<String, String> properties) {
    if (stopped) throw new IllegalStateException("Attempting to use a stopped persistence plugin.");
    ProbeDescription probe;
    try {
      String query = "select distinct pd from ProbeDescription pd where pd.probeName = :pname and pd.target.targetName = :tname " +
              "and pd.target.targetType = :ttype and pd.bm.bmClass = :bmClass and pd.bm.bmName = :bmName";
      String probeName = properties.get(Const.PROBE_NAME);
      String targetName = properties.get(Const.PROBE_TARGET_NAME);
      String targetType = properties.get(Const.PROBE_TARGET_TYPE);
      String bmClass = properties.get(Const.PROBE_BM_CLASS);
      String bmName = properties.get(Const.PROBE_BM_NAME);

      List<ProbeDescription> resultList = null;
      assert resultList.size() <= 1 : "There should be maximum of one probe description in the database with unique probe name, target type, target name, bm class and bm name." +
              " had " + resultList.size() + " for {" + probeName + "," + targetType + "," + targetName + "," + bmClass + "," + bmName + "}";
      if (resultList.size() == 1) {
        probe = resultList.get(0);
        //we need to update the endpoint address since that may have changed.. :)
        //todo: rollbacks
        transaction.begin();
        probe.updateEndpoint(properties);
        em.persist(probe);
        transaction.commit();
        return probe;
      }
      //get the target object for the probedescription
      TargetDescription target = createTargetDescription(properties);
      //get the bm description for the probedescription
      BMDescription bm = createBMDescription(properties);

      transaction.begin();
      probe = new ProbeDescription(properties, target, bm);
      em.persist(probe);
      transaction.commit();
    } finally {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      em.close();
    }
    return probe;
  }

  /**
   * Retrieves a BMDescription for the given properties. If one is found in the database, it is
   * returned. If not, a new one is created, stored into the database, and returned. The relevant values are
   * target name, target type, bm class, and bm name.
   *
   * @param properties Information for the BMDescription to be created.
   * @return The BMDescription matching the given arguments.
   */
  public BMDescription createBMDescription(Map<String, String> properties) {
    if (stopped) throw new IllegalStateException("Attempting to use a stopped persistence plugin.");
    BMDescription bm;
    try {
      Query query = em.createQuery("select distinct bm from BMDescription bm where bm.target.targetName = :tname " +
              "and bm.target.targetType = :ttype and bm.bmClass = :bmClass and bm.bmName = :bmName");
      String targetType = properties.get(Const.PROBE_TARGET_TYPE);
      String targetName = properties.get(Const.PROBE_TARGET_NAME);
      String bmClass = properties.get(Const.PROBE_BM_CLASS);
      String bmName = properties.get(Const.PROBE_BM_NAME);
      String bmDescription = properties.get(Const.PROBE_BM_DESCRIPTION);
      if (targetType == null || targetName == null || bmClass == null || bmName == null) {
        throw new IllegalArgumentException("BM cannot be created with null values for any of TargetType, TargetName, BMClass, BMName. " +
                "Got "+targetType+", "+targetName+", "+bmClass+", "+bmName+".");
      }
      query.setParameter("tname", targetName);
      query.setParameter("ttype", targetType);
      query.setParameter("bmClass", bmClass);
      query.setParameter("bmName", bmName);

      List<BMDescription> resultList = query.getResultList();
      assert resultList.size() <= 1 : "There should be maximum of one BM description in the database with unique target type, target name, bm class and bm name." +
              " had " + resultList.size() + " for {" + targetType + "," + targetName + "," + bmClass + "," + bmName + "}";
      if (resultList.size() == 1) {
        return resultList.get(0);
      }
      //get the target object for the probedescription
      TargetDescription target = createTargetDescription(properties);
      em.getTransaction().begin();
      bm = new BMDescription(target, bmClass, bmName, bmDescription);
      em.persist(bm);
      em.getTransaction().commit();
    } finally {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      em.close();
    }
    return bm;
  }

  /**
   * Retrieves a TargetDescription for the given properties. If one is found in the database, it is
   * returned. If not, a new one is created, stored into the database, and returned. The relevant values are
   * target name and target type.
   *
   * @param properties Information for the TargetDescription to be created.
   * @return The TargetDescription matching the given arguments.
   */
  public TargetDescription createTargetDescription(Map<String, String> properties) {
    if (stopped) throw new IllegalStateException("Attempting to use a stopped persistence plugin.");
    EntityManager em = emf.createEntityManager();
    TargetDescription target;
    try {
      Query query = em.createQuery("select distinct t from TargetDescription t where t.targetName = :tname " +
              "and t.targetType = :ttype");
      String targetType = properties.get(Const.PROBE_TARGET_TYPE);
      String targetName = properties.get(Const.PROBE_TARGET_NAME);
      if (targetType == null || targetName == null) {
        throw new IllegalArgumentException("Target cannot be created with null values for any of TargetType, TargetName. " +
                "Got "+targetType+", "+targetName+".");
      }

      query.setParameter("tname", targetName);
      query.setParameter("ttype", targetType);

      List<TargetDescription> resultList = query.getResultList();
      assert resultList.size() <= 1 : "There should be maximum of one target description in the database with unique target name,and target type." +
              " had " + resultList.size() + " for {" + targetType + "," + targetName + "}";
      if (resultList.size() == 1) {
        return resultList.get(0);
      }
      em.getTransaction().begin();
      target = new TargetDescription(targetType, targetName);
      em.persist(target);
      em.getTransaction().commit();
    } finally {
      if (em.getTransaction().isActive()) {
        em.getTransaction().rollback();
      }
      em.close();
    }
    return target;
  }
}
