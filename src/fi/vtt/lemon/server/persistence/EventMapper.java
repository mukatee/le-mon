package fi.vtt.lemon.server.persistence;

import fi.vtt.lemon.server.data.Event;
import fi.vtt.lemon.server.data.Value;
import org.apache.ibatis.annotations.Arg;
import org.apache.ibatis.annotations.ConstructorArgs;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.Date;

/**
 * @author Teemu Kanstren
 */
public interface EventMapper {
  @Select("SELECT * FROM event")
  @ConstructorArgs({
          @Arg(column="message", javaType=String.class),
          @Arg(column="event_source", javaType=String.class),
          @Arg(column="event_time", javaType=Date.class),
          @Arg(column="event_type", javaType=int.class)
  })
  public Event selectEvent(int id);
  
  @Insert("INSERT INTO event (message, event_source, event_time, event_type) VALUES (#{message}, #{sourceURI}, #{time}, #{type})")
  public void insert(Event event);
  
  
}
