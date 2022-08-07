package nextstep.auth.authentication.chain;


import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.provider.JwtAuthenticationProvider;
import nextstep.auth.context.Authentication;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class BearerTokenAuthenticationFilter extends AuthenticationChainFilter {

    @Qualifier("jwtAuthenticationProvider")
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        return new AuthenticationToken(null, token);
    }

    @Override
    public Authentication authentication(AuthenticationToken authenticationToken) {
        return jwtAuthenticationProvider.authenticate(authenticationToken);
    }
}
