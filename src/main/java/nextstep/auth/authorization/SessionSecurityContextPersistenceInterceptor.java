package nextstep.auth.authorization;

import nextstep.auth.authorization.strategy.SecurityContextHolderStrategy;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SessionSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {
    private final SecurityContextHolderStrategy strategy;

    public SessionSecurityContextPersistenceInterceptor(SecurityContextHolderStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return true;
        }

        SecurityContext securityContext = strategy.extract(request);
        strategy.setContext(securityContext);
        return true;
    }
}
