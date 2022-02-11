package nextstep.auth.authentication.converter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AuthenticationConverterConfig {

    public static final String SESSION_KEY = "session";
    public static final String TOKEN_KEY = "token";

    @Bean
    public AuthenticationConverter session() {
        return new SessionAuthenticationConverter();
    }

    @Bean
    public AuthenticationConverter token() {
        return new TokenAuthenticationConverter();
    }
}
