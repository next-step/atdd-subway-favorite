package nextstep.auth.model.authorization.interceptor;

import nextstep.auth.model.context.SecurityContext;
import nextstep.auth.model.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractSecurityContextPersistenceInterceptor implements SecurityContextPersistenceInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if (isNotNull(SecurityContextHolder.getContext().getAuthentication())) {
            return true;
        }

        SecurityContext securityContext = extractSecurityContext(request);
        if (isNotNull(securityContext)) {
            SecurityContextHolder.setContext(securityContext);
        }

        return true;
    }

    private boolean isNotNull(Object object) {
        return object != null;
    }

    protected abstract SecurityContext extractSecurityContext(HttpServletRequest request);
}
