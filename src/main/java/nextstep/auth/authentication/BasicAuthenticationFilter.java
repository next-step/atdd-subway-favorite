package nextstep.auth.authentication;

import nextstep.auth.authentication.exception.AuthenticationException;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthenticationFilter extends InterceptorChainingFilter {
    private final UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected UserDetails getUserDetails(HttpServletRequest request) {
        AuthenticationToken token = getToken(request);

        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getPrincipal());

        if (userDetails == null || !userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }

        return userDetails;
    }

    private AuthenticationToken getToken(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        if (splits.length < 2) {
            throw new IndexOutOfBoundsException();
        }
        String principal = splits[0];
        String credentials = splits[1];

        return new AuthenticationToken(principal, credentials);
    }
}
