package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.exception.UnauthorizedException;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.userdetails.User;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthenticationFilter extends AuthenticationChainingFilter {

    private static final String BEARER_DELIMITER = "Bearer ";
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
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorization == null || !authorization.startsWith(BEARER_DELIMITER)) {
            throw new AuthenticationException();
        }

        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (!jwtTokenProvider.validateToken(token)) {
            throw new UnauthorizedException();
        }

        String principal = jwtTokenProvider.getPrincipal(token);
        return new AuthenticationToken(principal, token);
    }
}
