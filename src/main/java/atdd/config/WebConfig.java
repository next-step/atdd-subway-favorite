package atdd.config;

import atdd.user.dto.JwtTokenInfo;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

    @Bean
    @ConfigurationProperties("security.jwt.token")
    public JwtTokenInfo jwtTokenInfo() {
        return new JwtTokenInfo();
    }

}
