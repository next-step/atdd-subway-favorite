package nextstep.auth.filters;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthenticationFilter extends AuthenticationSavingFilter<String> {
    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected String convert(HttpServletRequest request) {
        return AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
    }

    @Override
    protected UserDetails validate(String token) {
        boolean isValidToken = jwtTokenProvider.validateToken(token);
        if (!isValidToken) {
            throw new AuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(token);
        List<String> roles = jwtTokenProvider.getRoles(token);
        return User.of(principal, roles);
    }
}
