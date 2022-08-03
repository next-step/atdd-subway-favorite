package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthenticationFilter extends ChainInterceptor {
    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        validationToken(token);

        return createAuthentication(token);
    }

    private void validationToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }
    }

    private Authentication createAuthentication(String token) {
        String principal = jwtTokenProvider.getPrincipal(token);
        List<String> roles = jwtTokenProvider.getRoles(token);

        Authentication authentication = new Authentication(principal, roles);
        return authentication;
    }
}
