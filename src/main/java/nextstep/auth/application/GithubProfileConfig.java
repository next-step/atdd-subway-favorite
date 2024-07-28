package nextstep.auth.application;

import feign.codec.ErrorDecoder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients
@Configuration
public class GithubProfileConfig {
  @Bean
  public ErrorDecoder errorDecoder() {
    return new GithubErrorDecoder();
  }
}
