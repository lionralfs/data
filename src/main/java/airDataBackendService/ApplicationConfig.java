package airDataBackendService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ApplicationConfig {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public ObjectMapper jacksonObjectMapper() {
        return new ObjectMapper()/* .setPropertyNamingStrategy(propertyNamingStrategy()) */;
    }

}
