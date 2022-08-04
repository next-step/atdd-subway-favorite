package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthenticationFilter extends ChainingFilter {
    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
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

    @Override
    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        String credentials = token.getCredentials();
        List<String> roles = jwtTokenProvider.getRoles(credentials);
        return new Authentication(principal, roles);
    }


}
