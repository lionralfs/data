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

    @GetMapping(value = "latestData")
    public ResponseEntity<String> getLatestAirData() throws JsonProcessingException {
        return ResponseEntity.ok(objectMapper.writeValueAsString(airDataHandler.getLatestData()));
    }

    @GetMapping(value = "testDataJson")
    public ResponseEntity<String> getJsonTestData() throws JsonProcessingException {
        JsonDummyDataProducer producer = new JsonDummyDataProducer();
        String JsonString = objectMapper.writeValueAsString(producer.getJsonDummy());
        return ResponseEntity.ok(JsonString);
    }

    @GetMapping(value = "query")
    public ResponseEntity<String> getDataWithQuery(
            @RequestParam(value = "country", required = false) String country,
            @RequestParam(value = "sensorType", required = false) String sensorType,
            @RequestParam(value = "area", required = false) String area,
            @RequestParam(value = "box", required = false) String box
    ) throws JsonProcessingException {
        String url = "http://api.luftdaten.info/static/v1/filter/";
        if (country != null) {
            url = url + "country=" + country;
        }
        if (area != null) {
            if (country != null) {
                url = url + "&";
            }
            url = url + "area=" + area;
        }
        if (box != null) {
            if (country != null || area != null) {
                url = url + "&";
            }
            url = url + "box=" + box;
        }
        return ResponseEntity.ok(objectMapper.writeValueAsString(airDataHandler.getDataWithQuery(url)));
    }
}

