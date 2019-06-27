package airDataBackendService.database;

import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "sensors")
public class Sensor {
    @Field("sensor_id")
    public String id;

    @Field("lat")
    public double lat;

    @Field("lon")
    public double lon;
}