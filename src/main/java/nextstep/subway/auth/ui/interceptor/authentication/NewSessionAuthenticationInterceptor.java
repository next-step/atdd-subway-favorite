package nextstep.subway.auth.ui.interceptor.authentication;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class NewSessionAuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationConverter converter;

    public NewSessionAuthenticationInterceptor(AuthenticationConverter converter) {
        this.converter = converter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        return false;
    }
}
