package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthenticationFilter extends AuthenticationChainingFilter {
    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public User findUserDetails(AuthenticationToken token) {
        String credentials = token.getCredentials();
        List<String> roles = jwtTokenProvider.getRoles(credentials);
        return new User(token.getPrincipal(), credentials, roles);
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(token);
        return new AuthenticationToken(principal, token);
    }
}
