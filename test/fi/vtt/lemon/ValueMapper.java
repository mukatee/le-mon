package fi.vtt.lemon;

import fi.vtt.lemon.server.Value;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * @author Teemu Kanstren
 */
public interface ValueMapper {
//  @Select("SELECT * FROM bm_value, bm_description where bm_description.bm_id = #{id}")
  @Select("SELECT * FROM bm_value, bm_description where bm_description.bm_id = bm_value.bm_id")
  @ConstructorArgs({
          @Arg(column="measure_uri", javaType=String.class),
          @Arg(column="value_precision", javaType=int.class),
          @Arg(column="value_string", javaType=String.class),
          @Arg(column="value_time", javaType=Date.class)
  })
//  @Results(value = {
//          @Result(property="measureURI", column="measure_uri"),
//          @Result(property="precision", column="value_precision"),
//          @Result(property="time", column="value_time"),
//          @Result(property="value", column="value_string"),
//  })
  public Value selectValue(int id);
}
