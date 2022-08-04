package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.user.User;
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
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        if (splits.length < 2) {
            throw new IndexOutOfBoundsException();
        }
        String principal = splits[0];
        String credentials = splits[1];

        AuthenticationToken token = new AuthenticationToken(principal, credentials);

        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getPrincipal());

        if (userDetails == null || !userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }

        return userDetails;
    }

    @Override
    protected void setAuthentication(UserDetails userDetails) {
        Authentication authentication = new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
