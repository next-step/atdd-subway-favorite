package nextstep.auth.handler;

import nextstep.auth.user.User;
import nextstep.auth.user.UserDetailsService;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class UsernamePasswordAuthenticationHandler extends AuthenticationHandler {


    public UsernamePasswordAuthenticationHandler(UserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    public User preAuthentication(HttpServletRequest request) {
        final String username = request.getParameter("username");
        final String password = request.getParameter("password");

        return findAuthMember(username, password);
    }

    @Override
    public void afterAuthentication(User user, HttpServletResponse response) {
        Authentication authentication = new Authentication(user.getPrincipal(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
