package airDataBackendService.database;

import org.springframework.data.mongodb.core.mapping.Field;

public class Measurement {

  @Field("timestamp")
  public long timestamp;

  @Field("P10")
  public double p10;

  @Field("P25")
  public double p25;
}