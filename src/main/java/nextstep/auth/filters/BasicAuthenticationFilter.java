package nextstep.auth.filters;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.filters.provider.AuthenticationProvider;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthenticationFilter extends AuthenticationSavingFilter<AuthenticationToken> {
    private static final String DELIMITER = ":";
    private static final int AUTH_HEADER_LENGTH = 2;

    public BasicAuthenticationFilter(AuthenticationProvider<AuthenticationToken> authenticationProvider) {
        super(authenticationProvider);
    }

    @Override
    protected AuthenticationToken convert(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(DELIMITER);
        if (splits.length != AUTH_HEADER_LENGTH) {
            throw new AuthenticationException();
        }

        String principal = splits[0];
        String credentials = splits[1];

        return new AuthenticationToken(principal, credentials);
    }
}
