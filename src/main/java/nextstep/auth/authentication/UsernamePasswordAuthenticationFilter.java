package nextstep.auth.authentication;

import nextstep.auth.UserDetails;
import nextstep.auth.UserDetailsService;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends AuthenticationNonChainingFilter {
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    private final UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected UserDetails convert(HttpServletRequest request) {
        String principal = request.getParameter(USERNAME_FIELD);
        String credentials = request.getParameter(PASSWORD_FIELD);
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);

        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(credentials)) {
            throw new AuthenticationException();
        }
        return userDetails;
    }

    @Override
    protected void authenticate(UserDetails userDetails, HttpServletResponse response) {
        Authentication authentication = new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
