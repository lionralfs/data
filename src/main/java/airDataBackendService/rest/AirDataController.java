package airDataBackendService.rest;


import airDataBackendService.util.JsonDummyDataProducer;
import airDataBackendService.values.SensorData.Location;
import airDataBackendService.values.SensorData.SensorData;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "api/measurements")
public class AirDataController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AirDataHandler airDataHandler;

    @Value("${changeable.multiplierTime}")
    private long multiplierTime;

    @GetMapping(value= "latestData")
    public ResponseEntity<String> getLatestAirData() throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString(airDataHandler.getLatestData()));
    }

    @GetMapping(value="testDataJson")
    public ResponseEntity<String> getJsonTestData() throws JsonProcessingException {
        JsonDummyDataProducer producer = new JsonDummyDataProducer();
        String JsonString = objectMapper.writeValueAsString(producer.getJsonDummy());
        return ResponseEntity.ok(JsonString);
    }
}
