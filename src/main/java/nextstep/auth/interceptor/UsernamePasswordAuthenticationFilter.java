package nextstep.auth.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;

@RequiredArgsConstructor
public class UsernamePasswordAuthenticationFilter extends AuthenticationNonChainHandler {
    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    private final UserDetailsService userDetailsService;

    @Override
    protected AuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        return new AuthenticationToken(request.getParameter(USERNAME_FIELD), request.getParameter(PASSWORD_FIELD));
    }

    @Override
    protected UserDetails getUserDetails(AuthenticationToken authenticationToken) {
        return userDetailsService.loadUserByUsername(authenticationToken.getPrincipal());
    }

    @Override
    protected void afterHandle(UserDetails userDetails, HttpServletResponse response) {
        Authentication authentication = new Authentication(userDetails.getEmail(), userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
