package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthenticationFilter extends AuthenticationChainHandler {
    private final UserDetailsService userDetailsService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected String extractCredentials(HttpServletRequest request) {
        return AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
    }

    @Override
    protected UserDetails getUserDetails(String authCredentials) {
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        AuthenticationToken token = new AuthenticationToken(principal, credentials);

        return userDetailsService.loadUserByUsername(token.getPrincipal());
    }

    @Override
    protected Authentication createAuthentication(UserDetails userDetails) {
        return new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
    }
}
