package nextstep.auth.authentication;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;

public class BearerTokenAuthenticationFilter extends AuthenticationChainInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if (!jwtTokenProvider.validateToken(authCredentials)) {
            throw new AuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(authCredentials);
        List<String> roles = jwtTokenProvider.getRoles(authCredentials);

        return new Authentication(principal, roles);
    }
}
