package nextstep.auth.authentication;

import static nextstep.exception.ExceptionMagicString.NO_PASSWORD_IN_SESSION;
import static nextstep.exception.ExceptionMagicString.NO_USER_IN_SESSION;

import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;

public class SessionAuthenticationConverter implements AuthenticationConverter {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String principal = Stream.of(request.getParameterValues(USERNAME_FIELD))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(NO_USER_IN_SESSION));
        String credentials = Stream.of(request.getParameterValues(PASSWORD_FIELD))
            .findFirst()
            .orElseThrow(() -> new IllegalArgumentException(NO_PASSWORD_IN_SESSION));

        return new AuthenticationToken(principal, credentials);
    }
}
