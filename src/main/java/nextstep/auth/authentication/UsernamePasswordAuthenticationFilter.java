package nextstep.auth.authentication;

import nextstep.auth.user.User;
import nextstep.auth.user.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends NoMoreProceedAuthenticationFilter {

    private UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String principal = request.getParameter("username");
        String credentials = request.getParameter("password");

        Authenticate authenticate = new Authenticate(userDetailsService);
        User user = authenticate.execute(principal, credentials);

        SaveAuthentication saveAuthentication = new SaveAuthentication(principal, credentials, user);
        saveAuthentication.execute();

        return proceed();
    }
}
