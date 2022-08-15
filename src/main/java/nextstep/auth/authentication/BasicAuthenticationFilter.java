package nextstep.auth.authentication;

import nextstep.auth.UserDetails;
import nextstep.auth.UserDetailsService;
import nextstep.auth.context.Authentication;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthenticationFilter extends AbstractValidateAuthenticationFilter {

    private final UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected Authentication getAuthenticationToken(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        AuthenticationToken token = new AuthenticationToken(principal, credentials);

        UserDetails userDetails = this.userDetailsService.loadUserByUsername(token.getPrincipal());
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
        return new Authentication(userDetails.getPrincipal(), userDetails.getAuthorities());
    }
}
