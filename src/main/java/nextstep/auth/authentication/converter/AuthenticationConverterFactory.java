package nextstep.auth.authentication.converter;

import java.util.Map;

import static nextstep.auth.AuthenticationConverterConfig.SESSION_KEY;
import static nextstep.auth.AuthenticationConverterConfig.TOKEN_KEY;

public class AuthenticationConverterFactory {

    public static AuthenticationConverter ofSession(Map<String, AuthenticationConverter> converters) {
        return converters.get(SESSION_KEY);
    }

    public static AuthenticationConverter ofToken(Map<String, AuthenticationConverter> converters) {
        return converters.get(TOKEN_KEY);
    }
}
