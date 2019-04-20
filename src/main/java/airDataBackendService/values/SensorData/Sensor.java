package airDataBackendService.values.SensorData;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "pin",
        "sensor_type",
        "id"
})
public class Sensor {

    @JsonProperty("pin")
    private String pin;
    @JsonProperty("sensor_type")
    private SensorType sensorType;
    @JsonProperty("id")
    private long id;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("pin")
    public String getPin() {
        return pin;
    }

    @JsonProperty("pin")
    public void setPin(String pin) {
        this.pin = pin;
    }

    @JsonProperty("sensor_type")
    public SensorType getSensorType() {
        return sensorType;
    }

    @JsonProperty("sensor_type")
    public void setSensorType(SensorType sensorType) {
        this.sensorType = sensorType;
    }

    @JsonProperty("id")
    public long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(long id) {
        this.id = id;
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
