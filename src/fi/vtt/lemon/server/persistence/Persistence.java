/*
 * Copyright (c) 2012 VTT
 */

package fi.vtt.lemon.server.persistence;

import fi.vtt.lemon.server.data.Event;
import fi.vtt.lemon.server.data.Value;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import osmo.common.log.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.testng.Assert.assertEquals;

/**
 * Persists measurement data, events, ... If someone implements this that is.
 * Currently it stores history in memory but that is not a very scalable solution.
 *
 * @author Teemu Kanstren
 */
public class Persistence {
  private final static Logger log = new Logger(Persistence.class);
  /** Key=measureURI, Value=list of measurement values for the measureURI. */
  private Map<String, Collection<Value>> histories = new HashMap<>();
  private SqlSessionFactory sessionFactory;

  public Persistence() {
    String resource = "fi/vtt/lemon/mybatis-config.xml";
    Properties props = new Properties();
    InputStream inputStream = null;
    try {
      inputStream = Resources.getResourceAsStream(resource);
      props.load(new FileInputStream("le-mon.properties"));
    } catch (IOException e) {
      log.error("Failed to initialize database connection, unable to read le-mon.properties", e);
      System.exit(2);
    }
    sessionFactory = new SqlSessionFactoryBuilder().build(inputStream, props);
    Configuration configuration = sessionFactory.getConfiguration();
    configuration.addMapper(ValueMapper.class);
    configuration.addMapper(EventMapper.class);
  }
  
  /**
   * Reads a set of values from the database according to the given criteria.
   *
   * @return The set of Value objects matching the given criteria.
   */
  public List<Value> getValues(Collection<String> bmIds) {
    //this is temporary hack to get some results..
    List<Value> results = new ArrayList<>();
    for (String id : bmIds) {
      Collection<Value> values = histories.get(id);
      if (values == null) {
        log.warn("No measures for:" + id);
        continue;
      }
      results.addAll(values);
    }
    return results;
  }

  public synchronized void store(Value value) {
    String measureURI = value.getMeasureURI();
    Collection<Value> history = histories.get(measureURI);
    if (history == null) {
      history = new ArrayList<>();
      histories.put(measureURI, history);
    }
    history.add(value);

    log.debug("Persisting BM Value:"+value);
    try (SqlSession session = sessionFactory.openSession()) {
      ValueMapper mapper = session.getMapper(ValueMapper.class);
      mapper.insert(value);
      session.commit();
    } catch (Exception e) {
      log.error("Failed to persist value", e);
    }
  }
  
  public void bmAdded(String uri) {
    log.debug("Persisting BM Add Event:"+uri);
    try (SqlSession session = sessionFactory.openSession()) {
      EventMapper mapper = session.getMapper(EventMapper.class);
      mapper.insert(new Event("New BM registered", uri, new Date(), Event.ET_BM_ADD));
      session.commit();
    } catch (Exception e) {
      log.error("Failed to persist event", e);
    }
  }
  
  public void bmRemoved(String uri) {
    log.debug("Persisting BM Remove Event:"+uri);
    try (SqlSession session = sessionFactory.openSession()) {
      EventMapper mapper = session.getMapper(EventMapper.class);
      mapper.insert(new Event("BM removed", uri, new Date(), Event.ET_BM_REMOVE));
      session.commit();
    } catch (Exception e) {
      log.error("Failed to persist event", e);
    }
  }
}
