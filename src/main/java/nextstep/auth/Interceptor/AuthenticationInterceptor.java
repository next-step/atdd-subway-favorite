package nextstep.auth.Interceptor;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.AuthService;
import nextstep.auth.domain.AuthServices;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    private final AuthServices authServices;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        AuthService authService = authServices.findAuth(header);
        authService.validate(header);

        return true;
    }
}