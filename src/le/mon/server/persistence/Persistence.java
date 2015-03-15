package le.mon.server.persistence;

import le.mon.server.data.Event;
import le.mon.server.data.Value;
import le.mon.server.data.Event;
import le.mon.server.data.Value;
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

/**
 * Persists measurement data, events, ... If someone implements this that is.
 * Currently it stores history in memory but that is not a very scalable solution.
 *
 * @author Teemu Kanstren
 */
public class Persistence {
  private final static Logger log = new Logger(Persistence.class);
  /** Key=measureURI, Value=list of measurement values for the measureURI. */
  private Map<String, LimitedQueue<Value>> histories = new HashMap<>();
  private LimitedQueue<Value> values = new LimitedQueue<>(100);
  private SqlSessionFactory sessionFactory;

  public Persistence() {
    String resource = "le/mon/mybatis-config.xml";
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
    LimitedQueue<Value> history = histories.get(measureURI);
    if (history == null) {
      history = new LimitedQueue<>(100);
      histories.put(measureURI, history);
    }
    history.add(value);
    values.add(value);

    log.debug("Persisting BM Value:"+value);
    try (SqlSession session = sessionFactory.openSession()) {
      ValueMapper mapper = session.getMapper(ValueMapper.class);
      mapper.insert(value);
      session.commit();
    } catch (Exception e) {
      log.error("Failed to persist value", e);
    }
  }

  public void probeAdded(String uri) {
    log.debug("Persisting Probe Add Event:"+uri);
    try (SqlSession session = sessionFactory.openSession()) {
      EventMapper mapper = session.getMapper(EventMapper.class);
      mapper.insert(new Event("New Probe registered", uri, new Date(), Event.ET_PROBE_ADD));
      session.commit();
    } catch (Exception e) {
      log.error("Failed to persist event", e);
    }
  }

  public void probeRemoved(String uri) {
    log.debug("Persisting Probe Remove Event:"+uri);
    try (SqlSession session = sessionFactory.openSession()) {
      EventMapper mapper = session.getMapper(EventMapper.class);
      mapper.insert(new Event("Probe removed", uri, new Date(), Event.ET_PROBE_REMOVE));
      session.commit();
    } catch (Exception e) {
      log.error("Failed to persist event", e);
    }
  }

  public void probeLost(String uri) {
    log.debug("Persisting Probe Lost Event:"+uri);
    try (SqlSession session = sessionFactory.openSession()) {
      EventMapper mapper = session.getMapper(EventMapper.class);
      mapper.insert(new Event("Probe lost", uri, new Date(), Event.ET_PROBE_LOST));
      session.commit();
    } catch (Exception e) {
      log.error("Failed to persist event", e);
    }
  }

  public void probeMissing(String uri) {
    log.debug("Persisting Probe Missing Event:"+uri);
    try (SqlSession session = sessionFactory.openSession()) {
      EventMapper mapper = session.getMapper(EventMapper.class);
      mapper.insert(new Event("Probe missing", uri, new Date(), Event.ET_PROBE_MISSING));
      session.commit();
    } catch (Exception e) {
      log.error("Failed to persist event", e);
    }
  }

  public void probeFound(String uri) {
    log.debug("Persisting Probe Found Event:"+uri);
    try (SqlSession session = sessionFactory.openSession()) {
      EventMapper mapper = session.getMapper(EventMapper.class);
      mapper.insert(new Event("Probe found", uri, new Date(), Event.ET_PROBE_FOUND));
      session.commit();
    } catch (Exception e) {
      log.error("Failed to persist event", e);
    }
  }
  public synchronized List<Value> getLatest(int count) {
    int max = values.size();
    if (max > count) max = count;
    return values.subList(values.size() - max, values.size());
  }
}
