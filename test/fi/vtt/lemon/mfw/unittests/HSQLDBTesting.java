/*
 * Copyright (C) 2010-2011 VTT Technical Research Centre of Finland.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package fi.vtt.lemon.mfw.unittests;

import fi.vtt.lemon.common.Const;
import fi.vtt.lemon.server.shared.datamodel.BMDescription;
import fi.vtt.lemon.server.shared.datamodel.ServerEvent;
import fi.vtt.lemon.server.shared.datamodel.ProbeDescription;
import fi.vtt.lemon.server.shared.datamodel.TargetDescription;
import fi.vtt.lemon.server.shared.datamodel.Value;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
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
    props.put(Const.PROBE_BM_CLASS, "bmclass");
    props.put(Const.PROBE_BM_NAME, "bmname");
    props.put(Const.PROBE_NAME, "probe name");
    props.put(Const.PROBE_TARGET_TYPE, "target type");
    props.put(Const.PROBE_TARGET_NAME, "target name");
    props.put(Const.PROBE_BM_DESCRIPTION, "bm description");
    props.put(Const.PROBE_PRECISION, "1");
    ProbeDescription pd = testing.createProbeDescription(props);
//    BMDescription bm = testing.createBMDescription(props);
//    ProbeDescription pd = testing.createProbeDescription(props);
    System.out.println("bmid:"+pd.getBm().getBmId()+" bm:"+pd.getBm());

  }

  /**
   * Creates a ProbeDescription for the given information. Checks the DB for an existing suitable description.
   * If none is found, a new one is created and stored into the database. Whichever succeeds, the result is returned.
   *
   * @param properties  The information describing the probe.
   * @return The ProbeDescription object matching the given information.
   */
  public ProbeDescription createProbeDescription(Map<String, String> properties) {
    return null;
  }

  /**
   * Retrieves a BMDescription for the given properties. If one is found in the database, it is
   * returned. If not, a new one is created, stored into the database, and returned. The relevant values are
   * target name, target type, bm class, and bm name.
   *
   * @param properties Information for the BMDescription to be created.
   * @return The BMDescription matching the given arguments.
   */
  public BMDescription createBMDescription(Map<String, String> properties) {
    return null;
  }

  public TargetDescription createTargetDescription(Map<String, String> properties) {
    return null;
  }
}
