package nextstep.auth.authentication.filter.processing;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.UserDetailsService;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsernamePasswordAuthenticationFilter extends AuthenticationProcessingFilter {

    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";
    private final UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected AuthenticationToken convert(HttpServletRequest request) throws IOException {
        var username = request.getParameter(USERNAME_FIELD);
        var password = request.getParameter(PASSWORD_FIELD);

        return new AuthenticationToken(username, password);
    }

    @Override
    protected Authentication authenticate(AuthenticationToken authenticationToken) {
        var userDetails = userDetailsService.loadUserByUsername(authenticationToken.getPrincipal());

        if (!userDetails.getPassword().equals(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }

        return new Authentication(userDetails.getUsername(), userDetails.getAuthorities());
    }

    @Override
    protected void processing(Authentication authentication, HttpServletResponse response) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
