package nextstep.subway.auth.ui.interceptor.authorization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.subway.auth.infrastructure.SecurityContextHolder;

public class SecurityContextInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
        Exception exception) {
        SecurityContextHolder.clearContext();
    }
}
