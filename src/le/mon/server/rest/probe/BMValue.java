package le.mon.server.rest.probe;

import le.mon.server.LemonServer;
import le.mon.server.MessagePooler;
import le.mon.server.registry.Registry;
import le.mon.server.tasks.MeasurementProcessor;
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
public class BMValue extends HttpServlet {
  private static Logger log = new Logger(BMValue.class);

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

    log.debug("BM value request received "+msg);
    MessagePooler pooler = LemonServer.getPooler();
    Registry registry = LemonServer.getRegistry();

    if (registry.check(auth)) {
      try {
        JSONObject json = new JSONObject(msg);
        log.debug(" [x] Received '" + json + "'");
        String type = (String) json.get(MSGTYPE);
        //TODO: check msg type is correct..
        Runnable task = new MeasurementProcessor(json);
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