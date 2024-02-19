package nextstep.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author : Rene Choi
 * @since : 2024/02/18
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
