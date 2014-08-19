package fi.vtt.lemon.server.webui.jettytest;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Teemu Kanstren
 */
public class MyHandler extends AbstractHandler {
  @Override
  public void handle(String s, Request baseReq, HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
    resp.setContentType("text/html;charset=utf-8");
    resp.setStatus(HttpServletResponse.SC_OK);
    baseReq.setHandled(true);
    resp.getWriter().println("<h1>Hello World</h1>");
  }


  public static void main(String[] args) throws Exception {
    Server server = new Server(8080);
    server.setHandler(new MyHandler());
    server.start();
    server.join();
  }
}
