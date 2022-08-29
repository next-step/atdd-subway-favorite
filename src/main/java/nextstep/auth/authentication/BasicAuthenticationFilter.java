package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import nextstep.auth.context.Authentication;
import nextstep.auth.user.UserDetailService;
import nextstep.auth.user.UserDetails;
import org.apache.tomcat.util.codec.binary.Base64;

public class BasicAuthenticationFilter extends AuthenticationChainInterceptor {
    private UserDetailService userDetailService;

    public BasicAuthenticationFilter(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        AuthenticationToken token = new AuthenticationToken(principal, credentials);
        UserDetails userDetails = userDetailService.loadUserByUsername(token.getPrincipal());
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }

        return new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
    }
}
