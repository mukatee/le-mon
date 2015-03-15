package le.mon;

import le.mon.server.data.Value;
import le.mon.server.persistence.ValueMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.testng.annotations.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import static org.testng.Assert.*;

/**
 * @author Teemu Kanstren
 */
public class MyBatisTesting {
  @Test
  public void insertAndRead() throws Exception {
    String resource = "le/mon/mybatis-config.xml";
    InputStream inputStream = Resources.getResourceAsStream(resource);
    Properties props = new Properties();
    props.load(new FileInputStream("le-mon.properties"));
    SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream, props);
    factory.getConfiguration().addMapper(ValueMapper.class);
    try (SqlSession session = factory.openSession()) {
      ValueMapper mapper = session.getMapper(ValueMapper.class);
      Value value = mapper.selectValue(2);
      assertEquals(value.valueString(), "data1", "Value read from DB");
      assertEquals(value.getMeasureURI(), "le-mon://hello", "Measure URI read from DB");
      assertEquals(value.getTimeFormatted(), "1.1.1970 3:14:04", "Time read from DB");
    }
  }
}
