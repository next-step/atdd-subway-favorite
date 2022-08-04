package nextstep.auth.interceptor.chain;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;

public class BasicAuthenticationFilter extends AuthenticationChainInterceptor {
    private UserDetailsService userDetailService;

    public BasicAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailService = userDetailsService;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        AuthenticationToken token = getToken(authHeader);

        UserDetails userDetails = userDetailService.loadUserByUsername(token.getPrincipal());
        validationLoginMember(token, userDetails);

        return new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
    }

    private AuthenticationToken getToken(String authHeader) {
        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        return new AuthenticationToken(principal, credentials);
    }

    private void validationLoginMember(AuthenticationToken token, UserDetails userDetails) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
