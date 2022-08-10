package nextstep.auth.authentication;

import nextstep.auth.UserDetails;
import nextstep.auth.UserDetailsService;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.TokenRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends AuthenticationNonChainingFilter {
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    protected TokenRequest convert(HttpServletRequest request) {
        String principal = request.getParameter(USERNAME_FIELD);
        String credentials = request.getParameter(PASSWORD_FIELD);
        return new TokenRequest(principal, credentials);
    }

    @Override
    protected void authenticate(UserDetails userDetails, HttpServletResponse response) {
        Authentication authentication = new Authentication(userDetails.getUserId(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
