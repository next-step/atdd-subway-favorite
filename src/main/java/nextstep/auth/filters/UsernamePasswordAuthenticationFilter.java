package nextstep.auth.filters;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.filters.provider.AuthenticationProvider;
import nextstep.auth.user.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends AuthenticationRespondingFilter {
    private static final String USERNAME_FIELD_NAME = "username";
    private static final String PASSWORD_FIELD_NAME = "password";

    public UsernamePasswordAuthenticationFilter(AuthenticationProvider<AuthenticationToken> authenticationProvider) {
        super(authenticationProvider);
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String principal = request.getParameter(USERNAME_FIELD_NAME);
        String credentials = request.getParameter(PASSWORD_FIELD_NAME);
        return new AuthenticationToken(principal, credentials);
    }

    @Override
    public void authenticate(UserDetails userDetails, HttpServletResponse response) {
        Authentication authentication = new Authentication(userDetails.getPrincipal(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
