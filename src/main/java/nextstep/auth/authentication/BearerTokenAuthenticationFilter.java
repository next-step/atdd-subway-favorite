package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthenticationFilter extends AbstractValidateAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected Authentication getAuthenticationToken(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if(!this.jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }

        String email = this.jwtTokenProvider.getPrincipal(token);
        List<String> roles = this.jwtTokenProvider.getRoles(token);

        return new Authentication(email, roles);
    }
}
