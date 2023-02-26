package nextstep.member.infrastructure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.exception.UnAuthorizedException;
import nextstep.member.application.AuthService;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final AuthService authService;

    public AuthInterceptor(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String accessToken = AuthExtractor.extract(request);
            authService.findMemberByToken(accessToken);
            return true;
        } catch (Exception e) {
            throw new UnAuthorizedException();
        }
    }
}
