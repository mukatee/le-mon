package fi.vtt.lemon.server.rest.probe;

import fi.vtt.lemon.server.LemonServer;
import fi.vtt.lemon.server.MessagePooler;
import fi.vtt.lemon.server.registry.Registry;
import fi.vtt.lemon.server.tasks.RegisterProcessor;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import osmo.common.log.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static fi.vtt.lemon.MsgConst.*;


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
        //TODO: check msg type is correct..
        Runnable task = new RegisterProcessor(json);
        pooler.schedule(task);
      } catch (JSONException e) {
        log.error("Failed to parse measurement v alue JSON", e);
        return;
      }
    } else {
      return;
    }
    return;
  }
}