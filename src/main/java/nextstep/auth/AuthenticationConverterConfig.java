package nextstep.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.authentication.converter.SessionAuthenticationConverter;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationConverterConfig {

    @Bean
    public AuthenticationConverter sessionAuthenticationConverter() {
        return new SessionAuthenticationConverter();
    }

    @Bean
    public AuthenticationConverter tokenAuthenticationConverter() {
        return new TokenAuthenticationConverter(new ObjectMapper());
    }
}
