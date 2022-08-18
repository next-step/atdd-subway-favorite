package nextstep.auth.authentication;

import nextstep.member.application.UserDetailsService;
import nextstep.member.domain.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class Authenticator implements HandlerInterceptor {

    private final UserDetailsService userDetailsService;

    public Authenticator(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken token = convert(request);
        User user = userDetailsService.loadUserByUsername(token.getPrincipal());

        checkAuthentication(user, token.getCredentials());
        authenticate(user, response);

        return false;
    }

    abstract public AuthenticationToken convert(HttpServletRequest request) throws IOException;

    abstract public void authenticate(User user, HttpServletResponse response) throws IOException;

    private void checkAuthentication(User user, String password) {
        if (!user.checkPassword(password)) {
            throw new AuthenticationException();
        }
    }
}
