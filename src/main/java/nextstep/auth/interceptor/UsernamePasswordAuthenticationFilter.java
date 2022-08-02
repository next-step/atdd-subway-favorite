package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends AuthenticationNonChainHandler {

    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    private final UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected AuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        String username = request.getParameter(USERNAME_FIELD);
        String password = request.getParameter(PASSWORD_FIELD);

        return new AuthenticationToken(username, password);
    }

    @Override
    protected UserDetails getUserDetails(AuthenticationToken authenticationToken) {
        return userDetailsService.loadUserByUsername(authenticationToken.getPrincipal());
    }

    @Override
    protected void afterHandle(UserDetails userDetails, HttpServletResponse response) {
        Authentication authentication = new Authentication(userDetails.getEmail(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
