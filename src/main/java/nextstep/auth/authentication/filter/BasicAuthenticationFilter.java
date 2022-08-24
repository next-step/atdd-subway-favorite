package nextstep.auth.authentication.filter;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import nextstep.auth.authentication.AuthenticationManager;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.handler.AuthenticationSuccessHandler;
import nextstep.auth.authentication.token.BasicAuthenticationToken;
import nextstep.auth.context.Authentication;
import org.apache.tomcat.util.codec.binary.Base64;

public class BasicAuthenticationFilter extends AbstractAuthenticationFilter {
    public BasicAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationSuccessHandler authenticationSuccessHandler) {
        super(authenticationManager, authenticationSuccessHandler);
    }

    @Override
    protected Authentication convert(HttpServletRequest request) throws IOException {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        return new BasicAuthenticationToken(principal, credentials);
    }
}
