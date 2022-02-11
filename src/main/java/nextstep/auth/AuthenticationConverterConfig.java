package nextstep.auth;

import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.authentication.converter.SessionAuthenticationConverter;
import nextstep.auth.authentication.converter.TokenAuthenticationConverter;
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
