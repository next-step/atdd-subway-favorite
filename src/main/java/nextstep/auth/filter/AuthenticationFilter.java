package nextstep.auth.filter;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public abstract class AuthenticationFilter implements HandlerInterceptor {
    private final LoginService loginService;

    public AuthenticationFilter(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String email = getEmail(request);

        if (!loginService.isUserExist(email)) {
            throw new AuthenticationException();
        }

        LoginMember member = loginService.loadUserByUsername(email);

        String password = getPassword(request);
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }

        execute(response, email, member.getAuthorities());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    protected abstract String getEmail(HttpServletRequest request);

    protected abstract String getPassword(HttpServletRequest request);

    protected abstract void execute(HttpServletResponse response, String email, List<String> authorities) throws IOException;
}
