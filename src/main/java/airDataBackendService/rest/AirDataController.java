package airDataBackendService.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;

@RestController
@RequestMapping(path = "airdata/")
public class AirDataController {


    @Value("${changeable.multiplierTime}")
    private long multiplierTime;

    @PutMapping(value = "postData/")
    public ResponseEntity<String> persistData() {
        return ResponseEntity.ok(multiplierTime+ "postData");
    }

    @GetMapping(value="getData/")
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
