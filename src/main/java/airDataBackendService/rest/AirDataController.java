package airDataBackendService.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(path = "airdata")
public class AirDataController {


    @Value("${changeable.multiplierTime}")
    private long multiplierTime;

    @PutMapping(value = "postData")
    public ResponseEntity<String> persistData() {
        return ResponseEntity.ok(multiplierTime+ "postData");
    }

    @GetMapping(value = "testData")
    public ResponseEntity<List<Double>> getP1TestData() {
        List<Double> testList = new ArrayList<>(Arrays.asList(9.38, 9.11, 8.99, 9.16, 9.20,
                                                              8.88, 9.00, 9.01, 8.78, 8.88,
                                                              8.95, 9.10, 9.11, 9.05, 9.50,
                                                              9.33, 9.69, 9.88, 10.22, 9.99));
        return ResponseEntity.ok(testList);
    }

    @GetMapping(value="getData")
    public ResponseEntity<String> getDataForTimespan(
            @RequestParam long location,
            @RequestParam long lat,
            @RequestParam long lon,
            @RequestParam long startTime,
            @RequestParam long endTime,
            @RequestParam long pm1,
            @RequestParam long pm2
    ){
        return ResponseEntity.ok(multiplierTime+"getData");
    }
}
