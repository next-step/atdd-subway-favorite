package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class UsernamePasswordAuthenticationFilter extends NonChainingFilter {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";
    private UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String username = paramMap.get(USERNAME_FIELD)[0];
        String password = paramMap.get(PASSWORD_FIELD)[0];

        return new AuthenticationToken(username, password);
    }

    @Override
    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
        if (userDetails == null) {
            throw new AuthenticationException();
        }
        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
        return new Authentication(userDetails.getPrincipal(), userDetails.getAuthorities());
    }

    @Override
    public void afterAuthenticated(Authentication authentication, HttpServletResponse response) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
