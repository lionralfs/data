package airDataBackendService.services;

import airDataBackendService.database.SensorDataForDatabase;
import airDataBackendService.database.SensorRepository;
import airDataBackendService.values.SensorData.SensorData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Component
public class AirDataHandlerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(AirDataHandlerService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private SensorRepository sensorRepository;

    @Value("${changeable.restUrl}")
    private String restUrl;

    public List<SensorData> getLatestData() {
        LOGGER.info("starting Restcall for last 5 minutes data.");
        List<SensorData> restcall = restTemplate.getForObject(restUrl, List.class);
        LOGGER.info("data successfully received.");
        return restcall;
    }

    @Scheduled(fixedRate = 300000)
    public void persistLatestAirData() {
        LOGGER.info("starting Restcall for last 5 minutes data.");
        SensorData[] restcall = restTemplate.getForObject(restUrl, SensorData[].class);
        LOGGER.info("data successfully received.");
        List<SensorDataForDatabase> newDataList = new ArrayList<>();
        for(SensorData data : restcall){
            newDataList.add(new SensorDataForDatabase(
                    data.getId(),
                    data.getTimestamp(),
                    data.getSensor().getId(),
                    data.getSensor().getPin(),
                    data.getSensor().getSensorType().getName(),
                    data.getSensor().getSensorType().getManufacturer(),
                    data.getLocation().getLongitude(),
                    data.getLocation().getAltitude(),
                    data.getLocation().getLatitude(),
                    data.getLocation().getCountry(),
                    data.getSensordatavalues()
            ));
        }
        sensorRepository.saveAll(newDataList);
        LOGGER.info("data persisted");
    }

    public List<SensorData> getDataWithQuery(String queryUrl) {
        LOGGER.info("starting Restcall with query.");
        List<SensorData> restcall = restTemplate.getForObject(queryUrl, List.class);
        LOGGER.info("data successfully received.");
        return restcall;
    }






}
