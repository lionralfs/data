package airDataBackendService.database;

import airDataBackendService.values.SensorData.Sensordatavalue;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.List;

@Entity
public class SensorDataForDatabase {
    @Id
    private long id;
    private String timeStamp;
    private long sensorID;
    private String sensorPin;
    private String sensorName;
    private String sensorManufacturer;
    private float locationLongitude;
    private float locationAltitude;
    private float locationLatitude;
    private String locationCountry;
    private float p1;
    private float p2;

    public SensorDataForDatabase() {

    }

    public SensorDataForDatabase(long idParam, String timeStampParam, long sensorIDParam, String sensorPinParam,
                                 String sensorNameParam, String sensorManufacturerParam, String locationLongitudeParam,
                                 String locationAltitudeParam, String locationLatitudeParam, String locationCountryParam,
                                 List<Sensordatavalue> sensordatavalue) {
        id = idParam;
        timeStamp = timeStampParam;
        sensorID = sensorIDParam;
        sensorPin = sensorPinParam;
        sensorName = sensorNameParam;
        sensorManufacturer = sensorManufacturerParam;
        if (locationLongitudeParam.equals("")) {
        } else {
            locationLongitude = Float.parseFloat(locationLongitudeParam);
        }
        if (locationAltitudeParam.equals("")) {
        } else {
            locationAltitude = Float.parseFloat(locationAltitudeParam);
        }
        if (locationLatitudeParam.equals("")) {
        } else {
            locationLatitude = Float.parseFloat(locationLatitudeParam);
        }
        locationCountry = locationCountryParam;
        float p1Param;
        float p2Param;
        for (Sensordatavalue value : sensordatavalue) {
            if (value.getValueType().equals("P1")) {
                p1Param = Float.parseFloat(value.getValue());
                p1 = p1Param;
            } else if (value.getValueType().equals("P2")) {
                p2Param = Float.parseFloat(value.getValue());
                p2 = p2Param;
            }
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public long getSensorID() {
        return sensorID;
    }

    public void setSensorID(long sensorID) {
        this.sensorID = sensorID;
    }

    public String getSensorPin() {
        return sensorPin;
    }

    public void setSensorPin(String sensorPin) {
        this.sensorPin = sensorPin;
    }

    public String getSensorName() {
        return sensorName;
    }

    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

    public String getSensorManufacturer() {
        return sensorManufacturer;
    }

    public void setSensorManufacturer(String sensorManufacturer) {
        this.sensorManufacturer = sensorManufacturer;
    }

    public float getLocationLongitude() {
        return locationLongitude;
    }

    public void setLocationLongitude(float locationLongitude) {
        this.locationLongitude = locationLongitude;
    }

    public float getLocationAltitude() {
        return locationAltitude;
    }

    public void setLocationAltitude(float locationAltitude) {
        this.locationAltitude = locationAltitude;
    }

    public float getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(float locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    public float getP1() {
        return p1;
    }

    public void setP1(float p1) {
        this.p1 = p1;
    }

    public float getP2() {
        return p2;
    }

    public void setP2(float p2) {
        this.p2 = p2;
    }
}
