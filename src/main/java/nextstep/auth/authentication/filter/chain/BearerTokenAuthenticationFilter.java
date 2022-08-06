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

@RequiredArgsConstructor
public class BearerTokenAuthenticationFilter extends AuthenticationChainFilter {

    private final ProviderManager providerManager;

    @Override
    protected AuthenticationToken createToken(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        return new AuthenticationToken(authCredentials, authCredentials);
    }

    @Override
    protected Authentication authenticate(AuthenticationToken token) {
        AuthenticationProvider authenticationProvider = providerManager.getAuthenticationProvider(ProviderType.JWT_TOKEN);
        return authenticationProvider.authenticate(token);
    }

}
