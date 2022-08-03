package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthenticationFilter extends AuthenticationChainingFilter {
    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void authenticate(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(token);
        List<String> authorities = jwtTokenProvider.getRoles(token);

        Authentication authentication = new Authentication(principal, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
