package airDataBackendService.services;

import airDataBackendService.database.Measurement;
import airDataBackendService.database.MeasurementRepository;
import airDataBackendService.database.SensorDataForDatabase;
import airDataBackendService.database.SensorRepository;
import airDataBackendService.util.Box;
import airDataBackendService.values.SensorData.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class AirDataHandlerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AirDataHandlerService.class);

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
            maxageDate = dateFormat.parse(maxage);
        } catch(ParseException pe) {
            pe.printStackTrace();
            return new ArrayList<Measurement>();
        }
        
        return measurementRepository.customQuery(limit, offset, Box.from(box), maxageDate);
    }

    // public List<SensorData> getLatestData() {
    //     LOGGER.info("starting Restcall for last 5 minutes data.");
    //     List<SensorData> restcall = restTemplate.getForObject(restUrl, List.class);
    //     LOGGER.info("data successfully received.");
    //     return restcall;
    // }

    // @Scheduled(fixedRate = 300000)
    // public void persistLatestAirData() {
    //     LOGGER.info("starting Restcall for last 5 minutes data.");
    //     SensorData[] restcall = restTemplate.getForObject(restUrl, SensorData[].class);
    //     LOGGER.info("data successfully received.");
    //     List<SensorDataForDatabase> newDataList = new ArrayList<>();
    //     for (SensorData data : restcall) {
    //         newDataList.add(new SensorDataForDatabase(data.getId(), data.getTimestamp(), data.getSensor().getId(),
    //                 data.getSensor().getPin(), data.getSensor().getSensorType().getName(),
    //                 data.getSensor().getSensorType().getManufacturer(), data.getLocation().getLongitude(),
    //                 data.getLocation().getAltitude(), data.getLocation().getLatitude(), data.getLocation().getCountry(),
    //                 data.getSensordatavalues()));
    //     }
    //     sensorRepository.saveAll(newDataList);
    //     LOGGER.info("data persisted");
    // }

    // public List<SensorData> getDataWithQuery(String queryUrl) {
    //     LOGGER.info("starting Restcall with query.");
    //     List<SensorData> restcall = restTemplate.getForObject(queryUrl, List.class);
    //     LOGGER.info("data successfully received.");
    //     return restcall;
    // }

}
