package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.exception.AuthenticationException;
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
    protected UserDetails createAuthentication(HttpServletRequest request) {
        String username = request.getParameter(USERNAME_FIELD);
        String password = request.getParameter(PASSWORD_FIELD);

        AuthenticationToken token = new AuthenticationToken(username, password);

        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getPrincipal());

        if (userDetails.isValidPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
        return userDetails;
    }

    @Override
    protected void afterHandle(UserDetails userDetails, HttpServletResponse response) {
        Authentication authentication = new Authentication(userDetails.getEmail(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
