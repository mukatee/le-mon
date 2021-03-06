package le.mon.server.persistence;

import le.mon.server.data.Value;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * @author Teemu Kanstren
 */
public interface ValueMapper {
  //  @Select("SELECT * FROM bm_value, bm_description where bm_description.bm_id = #{id}")
  @Select("SELECT * FROM bm_value, bm_description WHERE bm_value.value_id = #{id} AND bm_description.bm_id = bm_value.bm_id")
  @ConstructorArgs({
          @Arg(column = "measure_uri", javaType = String.class),
          @Arg(column = "value_string", javaType = String.class),
          @Arg(column = "value_time", javaType = Date.class)
  })
  public Value selectValue(int id);

  @Insert("INSERT INTO bm_value (value_time, value_string) VALUES (#{time}, #{value})")
  public void insert(Value value);
}
