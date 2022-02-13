package nextstep.auth.authorization.strategy;

import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

public abstract class SecurityContextHolderStrategy {
    public abstract SecurityContext extract(HttpServletRequest request);
    public void setContext(SecurityContext securityContext) {
        if (securityContext != null) {
            SecurityContextHolder.setContext(securityContext);
        }
    }
}
