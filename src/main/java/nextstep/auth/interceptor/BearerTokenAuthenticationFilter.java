package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;

public class BearerTokenAuthenticationFilter extends AuthenticationChainingFilter {
    private static final String BEARER = "Bearer ";

    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(BEARER)) {
            throw new AuthenticationException();
        }

        String accessToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (!jwtTokenProvider.validateToken(accessToken)) {
            throw new AuthenticationException();
        }

        return new Authentication(jwtTokenProvider.getPrincipal(accessToken), jwtTokenProvider.getRoles(accessToken));
    }
}
