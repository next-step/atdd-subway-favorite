package nextstep.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class NotProgressInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler){
        try {
            execute(request, response);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    protected abstract void execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
