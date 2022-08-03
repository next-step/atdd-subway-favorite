package nextstep.auth.filter;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationFilter implements HandlerInterceptor {
    private final AuthenticationFilterStrategy strategy;
    private final LoginService loginService;

    public AuthenticationFilter(AuthenticationFilterStrategy strategy, LoginService loginService) {
        this.strategy = strategy;
        this.loginService = loginService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Authentication user = strategy.getAuthentication(request);

        Authentication authentication = getAuthentication(user);

        strategy.responseOk(response, (String) authentication.getPrincipal(), authentication.getAuthorities());
        return false;
    }

    private Authentication getAuthentication(Authentication authentication) {
        String email = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        LoginMember member = loginService.loadUserByUsername(email);

        if (member == null || !member.checkPassword(password)) {
            throw new AuthenticationException();
        }

        return new Authentication(member.getEmail(), member.getAuthorities());
    }

}
