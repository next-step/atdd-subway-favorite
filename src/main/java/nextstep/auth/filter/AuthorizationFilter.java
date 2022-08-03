package nextstep.auth.filter;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthorizationFilter implements HandlerInterceptor {

    private final AuthorizationStrategy strategy;

    public AuthorizationFilter(AuthorizationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        String token = strategy.getToken(request);

        if (token == null) {
            return true;
        }

        Authentication user = strategy.getAuthentication(token);

        // token을 이용해 사용자의 정보가 유효한지 확인합니다.
        if (!strategy.validUser(user)) {
            throw new AuthenticationException();
        }

        Authentication authentication = strategy.getAuthentication(user);

        // SecurityContext에 사용자 정보와 권한을 저장합니다.
        strategy.saveSecurityContext(authentication);

        return true;
    }
}
