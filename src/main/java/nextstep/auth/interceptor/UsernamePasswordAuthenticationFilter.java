package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends AuthenticationNonChainingFilter {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    protected AuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        return new AuthenticationToken(request.getParameter(USERNAME_FIELD), request.getParameter(PASSWORD_FIELD));
    }

    @Override
    protected void afterAuthentication(Authentication authentication, HttpServletResponse response) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
