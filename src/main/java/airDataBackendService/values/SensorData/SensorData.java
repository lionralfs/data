package airDataBackendService.values.SensorData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "sensordatavalues",
        "sampling_rate",
        "id",
        "sensor",
        "location",
        "timestamp"
})
public class SensorData {

    @JsonProperty("sensordatavalues")
    private List<Sensordatavalue> sensordatavalues = null;
    @JsonProperty("sampling_rate")
    private Object samplingRate;
    @JsonProperty("id")
    private long id;
    @JsonProperty("sensor")
    private Sensor sensor;
    @JsonProperty("location")
    private Location location;
    @JsonProperty("timestamp")
    private String timestamp;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("sensordatavalues")
    public List<Sensordatavalue> getSensordatavalues() {
        return sensordatavalues;
    }

    @JsonProperty("sensordatavalues")
    public void setSensordatavalues(List<Sensordatavalue> sensordatavalues) {
        this.sensordatavalues = sensordatavalues;
    }

    @JsonProperty("sampling_rate")
    public Object getSamplingRate() {
        return samplingRate;
    }

    @JsonProperty("sampling_rate")
    public void setSamplingRate(Object samplingRate) {
        this.samplingRate = samplingRate;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
    }

    @JsonProperty("sensor")
    public Sensor getSensor() {
        return sensor;
    }

    @JsonProperty("sensor")
    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonProperty("timestamp")
    public String getTimestamp() {
        return timestamp;
    }

    @JsonProperty("timestamp")
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
