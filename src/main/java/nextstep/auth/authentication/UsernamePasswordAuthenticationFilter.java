package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {

    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    private final UserDetailsService userDetailsService;

    public UsernamePasswordAuthenticationFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            final String email = request.getParameter(USERNAME);
            final String password = request.getParameter(PASSWORD);

            AuthenticationToken token = new AuthenticationToken(email, password);

            UserDetails loginMember = userDetailsService.loadUserByUsername(token.getPrincipal());

            if (!loginMember.checkPassword(token.getCredentials())) {
                throw new AuthenticationException();
            }

            Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception ignore) {
        }

        return true;
    }
}
