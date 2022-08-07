package nextstep.auth.authentication.chain;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.provider.AuthenticationProvider;
import nextstep.auth.context.Authentication;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
@RequiredArgsConstructor
public class BasicAuthenticationFilter extends AuthenticationChainFilter {

    @Qualifier("defaultAuthenticationProvider")
    private final AuthenticationProvider<AuthenticationToken> authenticationProvider;

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];
        return new AuthenticationToken(principal, credentials);
    }

    @Override
    public Authentication authentication(AuthenticationToken authenticationToken) {
        return authenticationProvider.authenticate(authenticationToken);
    }

}
