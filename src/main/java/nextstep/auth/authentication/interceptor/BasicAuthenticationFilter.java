package nextstep.auth.authentication.interceptor;

import javax.servlet.http.HttpServletRequest;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.authentication.user.UserDetails;
import nextstep.auth.authentication.user.UserDetailsService;
import org.apache.tomcat.util.codec.binary.Base64;

public class BasicAuthenticationFilter implements AuthenticationChainingFilter {
    private final UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        if (splits.length != 2) {
            throw new AuthenticationException();
        }

        String principal = splits[0];
        String credentials = splits[1];

        return new AuthenticationToken(principal, credentials);
    }

    @Override
    public UserDetails createUserDetails(AuthenticationToken token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getPrincipal());
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.isValid(token.getCredentials())) {
            throw new AuthenticationException();
        }

        return userDetails;
    }
}
