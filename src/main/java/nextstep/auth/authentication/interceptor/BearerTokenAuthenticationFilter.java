package nextstep.auth.authentication.interceptor;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.user.UserDetails;
import nextstep.member.domain.User;
import nextstep.auth.token.JwtTokenProvider;

public class BearerTokenAuthenticationFilter extends AuthenticationChainingFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String auth = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        return new AuthenticationToken(auth, auth);
    }

    @Override
    public UserDetails createUserDetails(AuthenticationToken token) {
        if (!jwtTokenProvider.validateToken(token.getCredentials())) {
            throw new AuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(token.getPrincipal());
        List<String> roles = jwtTokenProvider.getRoles(token.getPrincipal());

        return new User(principal, token.getCredentials(), roles);
    }
}
