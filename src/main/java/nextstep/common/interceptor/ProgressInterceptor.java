package nextstep.common.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ProgressInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler){
        try {
            execute(request, response);
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    protected abstract void execute(final HttpServletRequest request, final HttpServletResponse response);
}
