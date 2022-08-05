package nextstep.auth.authentication.filter.chain;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.provider.AuthenticationProvider;
import nextstep.auth.authentication.provider.ProviderManager;
import nextstep.auth.authentication.provider.ProviderType;
import nextstep.auth.context.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class BearerTokenAuthenticationFilter extends AuthenticationChainFilter {

    private final ProviderManager providerManager;

    @Override
    protected AuthenticationToken createToken(HttpServletRequest request) throws IOException {
        try {
            String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
            return new AuthenticationToken(authCredentials, authCredentials);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected Authentication authenticate(AuthenticationToken token) {
        AuthenticationProvider authenticationProvider = providerManager.getAuthenticationProvider(ProviderType.JWT_TOKEN);
        return authenticationProvider.authenticate(token);
    }

}
