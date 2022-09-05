package nextstep.auth.authentication;

import lombok.RequiredArgsConstructor;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.interceptor.AuthChainInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequiredArgsConstructor
public class BearerTokenAuthFilter extends AuthChainInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    protected void checkValidAuth(final AuthenticationToken token) {
        if (!jwtTokenProvider.validateToken(token.getPrincipal())) {
            throw new AuthenticationException();
        }
    }

    protected Authentication getAuthentication(final AuthenticationToken token) {
        String principal = jwtTokenProvider.getPrincipal(token.getPrincipal());
        List<String> roles = jwtTokenProvider.getRoles(token.getPrincipal());

        Authentication authentication = new Authentication(principal, roles);
        return authentication;
    }

    protected AuthenticationToken getAuthenticationToken(final HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        return new AuthenticationToken(authCredentials, authCredentials);
    }
}
