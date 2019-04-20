package airDataBackendService.rest;

import airDataBackendService.values.SensorData.SensorData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class AirDataHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(AirDataHandler.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${changeable.restUrlHamburg}")
    private String restUrl;

//  @Scheduled(cron="0 0 1 * * *")
    public List<SensorData> getLatestData() {
        LOGGER.info("starting Restcall for last 5 minutes data.");
        List<SensorData> restcall = restTemplate.getForObject(restUrl, List.class);
        LOGGER.info("data successfully received.");
        return restcall;
    }

    public List<SensorData> getDataWithQuery(String queryUrl) {
        LOGGER.info("starting Restcall with query.");
        List<SensorData> restcall = restTemplate.getForObject(queryUrl, List.class);
        LOGGER.info("data successfully received.");
        return restcall;
    }






}
