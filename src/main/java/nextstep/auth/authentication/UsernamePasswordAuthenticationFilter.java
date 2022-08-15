package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.interceptor.NonChainFilter;
import nextstep.member.user.User;
import nextstep.member.user.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends NonChainFilter {

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
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
