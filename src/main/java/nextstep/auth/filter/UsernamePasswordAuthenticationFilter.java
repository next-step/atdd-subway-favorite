package nextstep.auth.filter;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.userdetail.UserDetailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends AuthorizationFilter {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public UsernamePasswordAuthenticationFilter(UserDetailService userDetailService) {
        super(userDetailService);
    }

    @Override
    public AuthenticationToken getToken(HttpServletRequest request) {
        return new AuthenticationToken(request.getParameter(USERNAME), request.getParameter(PASSWORD));
    }

    @Override
    protected void postAuthenticate(Authentication authentication, HttpServletResponse response) {
        SecurityContextHolder.getContext()
                .setAuthentication(authentication);
    }

}
