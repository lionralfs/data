package airDataBackendService.services;

import airDataBackendService.database.DailyMeasurement;
import airDataBackendService.database.Measurement;
import airDataBackendService.database.Sensor;
import airDataBackendService.database.SensorRepository;
import airDataBackendService.database.MeasurementRepository;
import airDataBackendService.util.Box;
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

    public List<Measurement> getBySensor(String sensor, long timestamp) {
        return measurementRepository.getBySensor(sensor, timestamp);
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
