package airDataBackendService.util;

import airDataBackendService.values.SensorData.*;

import java.util.ArrayList;
import java.util.List;

public class JsonDummyDataProducer {

    public SensorData getJsonDummy() {
        SensorData dummyData = getSensorData();

        return dummyData;
    }

    private SensorData getSensorData() {
        SensorData data = new SensorData();
        data.setId(3411800398l);
        data.setSamplingRate(null);
        data.setTimestamp("2019-04-20 09:17:43");
        data.setLocation(getLocation());
        data.setSensor(getSensor());
        data.setSensordatavalues(getSensorDataValues());
        return data;
    }

    private List<Sensordatavalue> getSensorDataValues() {
        List<Sensordatavalue> valueList = new ArrayList<>();
        Sensordatavalue sensordatavalueOne = new Sensordatavalue();
        Sensordatavalue sensordatavalueTwo = new Sensordatavalue();
        sensordatavalueOne.setId(7237342149l);
        sensordatavalueTwo.setId(7237342153l);
        sensordatavalueOne.setValue("5.63");
        sensordatavalueTwo.setValue("4.63");
        sensordatavalueOne.setValueType("P1");
        sensordatavalueTwo.setValueType("P2");
        
        valueList.add(sensordatavalueOne);
        valueList.add(sensordatavalueTwo);
        
        return valueList;
    }

    private Sensor getSensor() {
        Sensor sensor = new Sensor();
        sensor.setId(20962);
        sensor.setPin("1");
        sensor.setSensorType(getSensorType());
        return sensor;
    }

    private SensorType getSensorType() {
        SensorType sensorType = new SensorType();
        sensorType.setId(14);
        sensorType.setName("SDS011");
        sensorType.setManufacturer("Nova Fitness");
        return sensorType;
    }

    private Location getLocation() {
        Location location = new Location();
        location.setId(10640);
        location.setLatitude("50.14");
        location.setLongitude("8.138");
        location.setAltitude("337.2");
        location.setCountry("DE");
        location.setExactLocation(0);
        return location;
    }
}
