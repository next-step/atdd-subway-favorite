package nextstep.auth.authentication;

import nextstep.member.application.UserDetailsService;
import nextstep.member.domain.LoginMember;
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
        LoginMember member = userDetailsService.loadUserByUsername(token.getPrincipal());

        checkAuthentication(member, token.getCredentials());
        authenticate(member, response);

        return false;
    }

    abstract public AuthenticationToken convert(HttpServletRequest request) throws IOException;

    abstract public void authenticate(LoginMember member, HttpServletResponse response) throws IOException;

    private void checkAuthentication(LoginMember member, String password) {
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }
    }
}
