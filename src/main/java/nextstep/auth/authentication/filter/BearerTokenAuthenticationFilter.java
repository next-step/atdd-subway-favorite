package nextstep.auth.authentication.filter;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import nextstep.auth.authentication.AuthenticationManager;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.handler.AuthenticationSuccessHandler;
import nextstep.auth.authentication.token.BearerAuthenticationToken;
import nextstep.auth.context.Authentication;

public class BearerTokenAuthenticationFilter extends AbstractAuthenticationFilter {
    public BearerTokenAuthenticationFilter(AuthenticationManager authenticationManager,
                                           AuthenticationSuccessHandler authenticationSuccessHandler) {
        super(authenticationManager, authenticationSuccessHandler);
    }

    @Override
    protected Authentication convert(HttpServletRequest request) throws IOException {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        return new BearerAuthenticationToken(authCredentials);
    }
}
