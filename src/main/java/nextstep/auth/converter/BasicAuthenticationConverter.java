package nextstep.auth.converter;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class BasicAuthenticationConverter implements AuthenticationConverter{
    @Override
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        return new AuthenticationToken(principal, credentials);
    }
}
