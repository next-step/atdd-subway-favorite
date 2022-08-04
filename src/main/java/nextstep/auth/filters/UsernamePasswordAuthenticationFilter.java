package nextstep.auth.filters;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.filters.provider.AuthenticationProvider;
import nextstep.auth.user.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends AuthenticationRespondingFilter {

    public UsernamePasswordAuthenticationFilter(AuthenticationProvider<AuthenticationToken> authenticationProvider) {
        super(authenticationProvider);
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String principal = request.getParameter("username");
        String credentials = request.getParameter("password");
        return new AuthenticationToken(principal, credentials);
    }

    @Override
    public void authenticate(UserDetails userDetails, HttpServletResponse response) {
        Authentication authentication = new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
