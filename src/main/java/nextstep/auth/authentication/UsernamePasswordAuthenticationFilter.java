package nextstep.auth.authentication;

import nextstep.auth.user.User;
import nextstep.auth.user.UserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    private UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            User user = userDetailsService.loadUserByUsername(username);
            SaveAuthentication saveAuthentication = new SaveAuthentication(username, password, user);
            saveAuthentication.execute();
            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
