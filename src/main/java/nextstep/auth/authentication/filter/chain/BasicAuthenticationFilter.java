package nextstep.auth.authentication.filter.chain;

import nextstep.auth.authentication.*;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicAuthenticationFilter extends ChainInterceptor {
    private UserDetailService userDetailService;

    public BasicAuthenticationFilter(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    protected AuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        return new AuthenticationToken(principal, credentials);
    }

    @Override
    protected Authentication getAuthentication(AuthenticationToken authenticationToken) {
        UserDetails userDetails = userDetailService.loadUserByUsername(authenticationToken.getPrincipal());
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }

        return new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
    }

    @Override
    protected void doAuthentication(Authentication authentication, HttpServletResponse response) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
