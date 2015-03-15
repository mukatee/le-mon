package le.mon.server.persistence;

import java.util.LinkedList;

/**
 * @author Teemu Kanstren
 */
public class LimitedQueue<T> extends LinkedList<T> {
  private int limit;

  public LimitedQueue(int limit) {
    this.limit = limit;
  }

  @Override
  public boolean add(T o) {
    super.add(o);
    while (size() > limit) { super.remove(); }
    return true;
  }
}
