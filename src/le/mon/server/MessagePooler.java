package le.mon.server;

import le.mon.Config;
import le.mon.MsgConst;
import le.mon.Config;
import le.mon.MsgConst;
import osmo.common.log.Logger;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author Teemu Kanstren
 */
public class MessagePooler {
  private final static Logger log = new Logger(MessagePooler.class);
  /** The thread pool for processing received messages. */
  private ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(Config.getInt(MsgConst.THREAD_POOL_SIZE, 5));

  public MessagePooler() {
  }

  public void schedule(Runnable task) {
    executor.execute(task);
  }
}
