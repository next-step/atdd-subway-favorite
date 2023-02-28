package nextstep.auth.Interceptor;

import lombok.RequiredArgsConstructor;
import nextstep.auth.domain.AuthType;
import nextstep.auth.domain.AuthTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class AuthenticationInterceptor extends HandlerInterceptorAdapter {
    private final AuthTypes authTypes;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        AuthType authType = authTypes.findAuth(header);
        authType.validate(header);

        return true;
    }
}