package nextstep.auth.authentication.filter.chain;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.provider.AuthenticationProvider;
import nextstep.auth.authentication.provider.ProviderManager;
import nextstep.auth.authentication.provider.ProviderType;
import nextstep.auth.authorization.extractor.AuthorizationExtractor;
import nextstep.auth.context.Authentication;

import javax.servlet.http.HttpServletRequest;

@RequiredArgsConstructor
public class BearerTokenAuthenticationFilter extends AuthenticationChainFilter {

    private final ProviderManager providerManager;
    private final AuthorizationExtractor authorizationExtractor;

    @Override
    protected AuthenticationToken createToken(HttpServletRequest request) {
        String authCredentials = authorizationExtractor.extract(request);
        return new AuthenticationToken(authCredentials, authCredentials);
    }

    @Override
    protected Authentication authenticate(AuthenticationToken token) {
        AuthenticationProvider authenticationProvider = providerManager.getAuthenticationProvider(ProviderType.JWT_TOKEN);
        return authenticationProvider.authenticate(token);
    }

}
