package nextstep.auth.config;

import nextstep.auth.config.exception.MissingTokenException;
import nextstep.auth.config.exception.ValidateTokenException;
import nextstep.auth.infra.JwtTokenProvider;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static nextstep.auth.config.message.AuthError.NOT_MISSING_TOKEN;
import static nextstep.auth.config.message.AuthError.NOT_VALID_TOKEN;

@Component
public class BearerAuthInterceptor implements HandlerInterceptor {

    private static final String EMAIL = "email";
    private static final String BEARER = "성";
    private final AuthorizationExtractor authorizationExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    public BearerAuthInterceptor(final AuthorizationExtractor authorizationExtractor, final JwtTokenProvider jwtTokenProvider) {
        this.authorizationExtractor = authorizationExtractor;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handle) {

        final String token = authorizationExtractor.extract(request, BEARER);
        if (token.isEmpty()) {
            throw new MissingTokenException(NOT_MISSING_TOKEN);
        }
        if (!jwtTokenProvider.validateToken(token)) {
            throw new ValidateTokenException(NOT_VALID_TOKEN);
        }
        final String principal = jwtTokenProvider.getPrincipal(token);
        request.setAttribute(EMAIL, principal);
        return true;
    }
}
