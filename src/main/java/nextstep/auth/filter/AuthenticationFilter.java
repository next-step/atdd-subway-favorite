package nextstep.auth.filter;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationFilter implements HandlerInterceptor {
    private final AuthenticationFilterStrategy authenticationStrategy;
    private final LoginService loginService;

    public AuthenticationFilter(AuthenticationFilterStrategy authenticationStrategy, LoginService loginService) {
        this.authenticationStrategy = authenticationStrategy;
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication authentication = authenticationStrategy.getAuthentication(request);

        String email = (String) authentication.getPrincipal();
        if (!loginService.isUserExist(email)) {
            throw new AuthenticationException();
        }

        LoginMember member = loginService.loadUserByUsername(email);
        String password = (String) authentication.getCredentials();
        if (!member.checkPassword(password)) {
            throw new AuthenticationException();
        }

        authenticationStrategy.execute(response, email, member.getAuthorities());
        return false;
    }

}
