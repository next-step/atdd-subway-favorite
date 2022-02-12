package nextstep.auth.authorization;

import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public abstract class SecurityContextInterceptor implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return true;
        }
        SecurityContext securityContext = getSecurityContext(request);
        if (Objects.isNull(securityContext)) {
            return true;
        }
        SecurityContextHolder.setContext(securityContext);
        return true;
    }

    @Override
    public void afterCompletion(final HttpServletRequest request,
                                final HttpServletResponse response,
                                final Object handler,
                                final Exception ex) {
        SecurityContextHolder.clearContext();
    }

    protected abstract SecurityContext getSecurityContext(final HttpServletRequest httpServletRequest);
}
