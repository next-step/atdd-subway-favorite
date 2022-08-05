package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthenticationFilter extends AuthenticationChainingFilter {

    private static final int PRINCIPAL_INDEX = 0;
    private static final int CREDENTIALS_INDEX = 1;
    private static final int USER_INFO_SIZE = 2;
    private static final String DELIMITER = ":";

    private UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public UserDetails findUserDetails(AuthenticationToken token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getPrincipal());
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.isValidCredentials(token.getCredentials())) {
            throw new AuthenticationException();
        }
        return userDetails;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(DELIMITER);
        if(isInValidSize(splits)) {
            throw new AuthenticationException();
        }

        String principal = splits[PRINCIPAL_INDEX];
        String credentials = splits[CREDENTIALS_INDEX];
        return new AuthenticationToken(principal, credentials);
    }

    private boolean isInValidSize(String[] splits) {
        return splits.length != USER_INFO_SIZE;
    }
}
