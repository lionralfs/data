package airDataBackendService.database;

import java.util.Date;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.format.annotation.DateTimeFormat;

@Document(collection = "sensordata")
public class DailyMeasurement {

  @Field("sensor_id")
  public String sensor_id;

  @DateTimeFormat
  @Field("day")
  public Date day;

  @Field("measurements")
  public List<Measurement> measurements;
}