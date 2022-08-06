package nextstep.auth.authentication;

import nextstep.auth.authentication.exception.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends InterceptorNoChainingFilter {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private final UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected UserDetails getUserDetails(HttpServletRequest request) {
        final String email = request.getParameter(USERNAME);
        final String password = request.getParameter(PASSWORD);

        AuthenticationToken token = new AuthenticationToken(email, password);

        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getPrincipal());

        if (userDetails == null || !userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }

        return userDetails;
    }

    @Override
    protected void setAuthentication(UserDetails userDetails, HttpServletResponse response) {
        Authentication authentication = new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
