package nextstep.auth.model.authentication.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthenticationInterceptor extends HandlerInterceptor {
    @Override
    boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException;
}
