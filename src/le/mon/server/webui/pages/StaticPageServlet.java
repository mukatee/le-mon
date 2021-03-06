package le.mon.server.webui.pages;

import osmo.common.TestUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Teemu Kanstren
 */
public class StaticPageServlet extends HttpServlet {
  private final String pageFile;

  public StaticPageServlet(String pageFile) {
    this.pageFile = pageFile;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showPage(req, resp);
  }

  private void showPage(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    PrintWriter out = resp.getWriter();
    String resource = TestUtils.getResource(StaticPageServlet.class, pageFile);
    out.write(resource);
  }
}
