package le.mon.server.rest.probe;

import le.mon.server.LemonServer;
import le.mon.server.MessagePooler;
import le.mon.server.registry.Registry;
import le.mon.server.tasks.KeepAliveProcessor;
import le.mon.server.tasks.RegisterProcessor;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static le.mon.MsgConst.*;


/**
 * @author Teemu Kanstren
 */
public class KeepAlive extends HttpServlet {
  private static Logger log = new Logger(KeepAlive.class);

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  private void showPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    String auth = req.getParameter("auth");
    String msg = req.getParameter("msg");

//    System.out.println("received:"+msg);

    log.debug("Keepalive request received "+msg);
    MessagePooler pooler = LemonServer.getPooler();
    Registry registry = LemonServer.getRegistry();

    if (registry.check(auth)) {
      try {
        JSONObject json = new JSONObject(msg);
        log.debug(" [x] Received '" + json + "'");
        String type = (String) json.get(MSGTYPE);
        String url = (String) json.get(PROBE_URL);
        Runnable task = new KeepAliveProcessor(json);
        pooler.schedule(task);
      } catch (JSONException e) {
        log.error("Failed to parse keepalive JSON", e);
        return;
      }
    } else {
      return;
    }
    return;
  }
}