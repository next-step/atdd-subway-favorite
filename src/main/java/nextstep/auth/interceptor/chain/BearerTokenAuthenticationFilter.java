package nextstep.auth.interceptor.chain;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthenticationFilter extends AuthenticationChainInterceptor {
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
