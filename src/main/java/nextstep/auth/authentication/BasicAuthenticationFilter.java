package nextstep.auth.authentication;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.provider.AuthenticationProvider;
import nextstep.auth.authentication.provider.ProviderManager;
import nextstep.auth.authentication.provider.ProviderType;
import nextstep.auth.context.Authentication;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class BasicAuthenticationFilter extends AuthenticationChainFilter {

    private final ProviderManager providerManager;

    @Override
    protected AuthenticationToken createToken(HttpServletRequest request) throws IOException {
        try {
            String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
            String authHeader = new String(Base64.decodeBase64(authCredentials));

            String[] splits = authHeader.split(":");
            String principal = splits[0];
            String credentials = splits[1];

            return new AuthenticationToken(principal, credentials);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected Authentication authenticate(AuthenticationToken token) {
        AuthenticationProvider authenticationProvider = providerManager.getAuthenticationProvider(ProviderType.USER_PASSWORD);
        return authenticationProvider.authenticate(token);
    }
}
