package airDataBackendService.rest;

import airDataBackendService.database.Measurement;
import airDataBackendService.services.AirDataHandlerService;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "api/measurements")
public class AirDataController {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    AirDataHandlerService airDataHandlerService;

    @GetMapping(value = "dust", produces = "application/json")
    public List<Measurement> getDustData(
            @RequestParam(value = "maxage", defaultValue = "2019-04-28T23:59:00.000Z") String maxage,
            @RequestParam(value = "box", required = false) String box,
            @RequestParam(value = "country", defaultValue = "DE") String country,
            @RequestParam(value = "limit", defaultValue = "1000") int limit,
            @RequestParam(value = "offset", defaultValue = "0") int offset) {
        return airDataHandlerService.getDustData(maxage, box, country, limit, offset);
    }
}
