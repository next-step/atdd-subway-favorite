package nextstep.auth.authentication.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.user.UserDetails;
import nextstep.auth.authentication.user.UserDetailsService;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;

public class UsernamePasswordAuthenticationFilter extends AuthenticationNotChainingFilter {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private final UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String username = request.getParameter(USERNAME);
        String password = request.getParameter(PASSWORD);
        return new AuthenticationToken(username, password);
    }

    @Override
    public UserDetails createUserDetails(AuthenticationToken token) {
        return userDetailsService.loadUserByUsername(token.getPrincipal());
    }

    @Override
    public void afterAuthentication(Authentication authenticate, HttpServletResponse response) {
        Authentication authentication = new Authentication(authenticate.getPrincipal(), authenticate.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
