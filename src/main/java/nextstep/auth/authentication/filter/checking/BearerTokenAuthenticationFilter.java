package nextstep.auth.authentication.filter.checking;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class BearerTokenAuthenticationFilter extends AuthenticationCheckingFilter {
    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected AuthenticationToken convert(HttpServletRequest request) throws IOException {
        return new AuthenticationToken(null, getToken(request));
    }

    @Override
    protected Authentication authenticate(AuthenticationToken authenticationToken) {
        var token = authenticationToken.getCredentials();

        if (!jwtTokenProvider.validateToken(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }

        var principal = jwtTokenProvider.getPrincipal(token);
        var roles = jwtTokenProvider.getRoles(token);

        return new Authentication(principal, roles);
    }

    private String getToken(HttpServletRequest request) {
        return request.getHeader("authorization").split(" ")[1];
    }
}
