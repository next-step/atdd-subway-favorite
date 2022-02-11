package nextstep.auth.authorization.strategy;

import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;

public interface SecurityContextHolderStrategy {
    SecurityContext extract(HttpServletRequest request);
    default void setContext(SecurityContext securityContext) {
        if (securityContext != null) {
            SecurityContextHolder.setContext(securityContext);
        }
    }
}
