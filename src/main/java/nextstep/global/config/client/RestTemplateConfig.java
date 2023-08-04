package nextstep.global.config.client;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        RestTemplate client = new RestTemplate();
        client.setErrorHandler(new RestTemplateResponseErrorHandler());

        return client;
    }

}
