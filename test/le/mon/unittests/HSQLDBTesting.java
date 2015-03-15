package le.mon.unittests;

import le.mon.MsgConst;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Teemu Kanstren
 */
public class HSQLDBTesting {
  public static void main_jdbc(String[] args) throws Exception {
    Class.forName("org.hsqldb.jdbcDriver");

    Connection conn = DriverManager.getConnection("jdbc:hsqldb:file:testdb", "sa", "");
    Statement st = conn.createStatement();
    st.executeUpdate("create table BM (BM_ID integer not null, BM_NAME varchar(32) not null, primary key (BM_ID))");
    PreparedStatement ps = conn.prepareStatement("insert into BM values (?, ?)");
    ps.setInt(1, 1);
    ps.setString(2, "bob");
    ps.executeUpdate();

    ResultSet rs = st.executeQuery("select * from BM");
    while (rs.next()) {
      int id = rs.getInt(1);
      String name = rs.getString(2);
      System.out.println("results:" + id + "," + name);
    }
  }

  public static void main(String[] args) {
    HSQLDBTesting testing = new HSQLDBTesting();
    Map<String, String> props = new HashMap<String, String>();
    props.put(MsgConst.PROBE_BM_CLASS, "bmclass");
    props.put(MsgConst.PROBE_BM_NAME, "bmname");
    props.put(MsgConst.PROBE_NAME, "probe name");
    props.put(MsgConst.PROBE_TARGET_TYPE, "target type");
    props.put(MsgConst.PROBE_TARGET_NAME, "target name");
    props.put(MsgConst.PROBE_BM_DESCRIPTION, "bm description");
    props.put(MsgConst.PROBE_PRECISION, "1");

  }
}
