package nextstep.auth;

import nextstep.auth.exception.EmptyAuthorizationHeaderException;
import nextstep.member.domain.exception.InvalidTokenException;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class AuthenticationHelper {

    public static final String BEARER_PREFIX = "Bearer ";

    public static String getToken(final HttpServletRequest request) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (Objects.isNull(authHeader)) {
            throw new EmptyAuthorizationHeaderException();
        }

        validateBearerToken(authHeader);

        return authHeader.substring(BEARER_PREFIX.length());
    }

    private static void validateBearerToken(final String authHeader) {
        if (!authHeader.toLowerCase().startsWith(BEARER_PREFIX.toLowerCase())) {
            throw new InvalidTokenException();
        }
    }
}
