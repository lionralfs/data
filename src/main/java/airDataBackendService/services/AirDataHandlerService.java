package airDataBackendService.services;

import airDataBackendService.database.DailyMeasurement;
import airDataBackendService.database.Measurement;
import airDataBackendService.database.Sensor;
import airDataBackendService.database.SensorRepository;
import airDataBackendService.database.MeasurementRepository;
import airDataBackendService.util.Box;
import airDataBackendService.rest.BySensorResponse;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Component
public class AirDataHandlerService {
    // private static final Logger LOGGER =
    // LoggerFactory.getLogger(AirDataHandlerService.class);

    @Autowired
    private SensorRepository sensorRepository;

    @Autowired
    private MeasurementRepository measurementRepository;

    @Value("${changeable.restUrl}")
    private String restUrl;

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    public List<Measurement> getDustData(String maxage, String box, String country, int limit, int offset) {
        System.out.println("[maxage]: " + maxage);
        System.out.println("[box]: " + box);
        System.out.println("[country]: " + country);
        System.out.println("[limit]: " + limit);
        System.out.println("[offset]: " + offset);

        if (limit < 1 || limit > 100000) {
            limit = 10000;
        }

        if (offset < 0) {
            offset = 0;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
        Date maxageDate = yesterday();
        try {
            if (maxage != null) {
                maxageDate = dateFormat.parse(maxage);
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
            return new ArrayList<Measurement>();
        }

        return measurementRepository.customQuery(limit, offset, Box.from(box), maxageDate);
    }

    public List<Sensor> getSensors() {
        return sensorRepository.findAll();
    }

    private boolean hasMeasurementWithinTimestampWithOffset(List<Measurement> measurements, long timestampInSeconds,
            long offsetInSeconds) {
        for (Measurement m : measurements) {
            if (Math.abs(m.timestamp - timestampInSeconds) <= offsetInSeconds) {
                return true;
            }
        }
        return false;
    }

    private boolean isContinuous(List<Measurement> measurements, long startTimeInSeconds) {
        long endTimeInSeconds = startTimeInSeconds - 7 * 24 * 60 * 60;
        long offsetInSeconds = 3 * 60 * 60;// 3 hours in seconds

        for (long i = startTimeInSeconds; i >= endTimeInSeconds; i -= 60 * 60) {
            if (!this.hasMeasurementWithinTimestampWithOffset(measurements, i, offsetInSeconds)) {
                return false;
            }
        }

        return true;
    }

    private Measurement bestFit(List<Measurement> measurements, long timestamp) {
        Measurement result = new Measurement();
        result.timestamp = Long.MAX_VALUE;

        for (Measurement m : measurements) {
            if (Math.abs(result.timestamp - timestamp) > Math.abs(m.timestamp - timestamp)) {
                result = m;
            }
        }

        return result;
    }

    public BySensorResponse getBySensor(String sensor, long timestamp) {
        // retrieve all relevant measurements from the database
        List<Measurement> allMeasurements = measurementRepository.getBySensor(sensor, timestamp);

        BySensorResponse response = new BySensorResponse();
        response.continuous = this.isContinuous(allMeasurements, timestamp);
        if (response.continuous) {
            response.measurement = this.bestFit(allMeasurements, timestamp);
        }
        return response;
    }

    public Map<String, String[]> getAverages(long timestamp) {
        List<DailyMeasurement> measurements = measurementRepository.getMeasurementsByDay(timestamp);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.setTimeInMillis((long) timestamp * 1000);
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), 0,
                0, 0);
        long startOfDay = calendar.getTimeInMillis() / 1000;

        if (measurements == null) {
            return new HashMap<String, String[]>(0);
        }

        Map<String, String[]> result = new HashMap<String, String[]>();
        for (DailyMeasurement dm : measurements) {
            String[] list = new String[24];
            Arrays.fill(list, "");

            for (Measurement m : dm.measurements) {
                int hour = (int) Math.floor((m.timestamp - startOfDay) / (60 * 60));

                if (hour < 0 || hour > 23) {
                    continue;
                }

                list[hour] = list[hour] + ";" + m.p25;
            }

            result.put(dm.sensor_id, list);
        }

        return result;
    }

}
