package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthenticationFilter extends AbstractValidateAuthenticationFilter<JwtTokenProvider> {

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        super(jwtTokenProvider);
    }

    @Override
    protected Authentication getAuthenticationToken(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if(!this.t.validateToken(token)) {
            throw new AuthenticationException();
        }

        String email = this.t.getPrincipal(token);
        List<String> roles = this.t.getRoles(token);

        return new Authentication(email, roles);
    }
}
