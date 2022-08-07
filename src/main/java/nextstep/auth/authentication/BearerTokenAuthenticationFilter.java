package nextstep.auth.authentication;

import nextstep.auth.authentication.exception.BearerAuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.user.User;
import nextstep.auth.user.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthenticationFilter extends InterceptorChainingFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected UserDetails getUserDetails(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new BearerAuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(token);
        List<String> roles = jwtTokenProvider.getRoles(token);

        return User.of(principal, roles);
    }
}
