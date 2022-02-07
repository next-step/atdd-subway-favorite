package nextstep.auth.authorization.converter;

import static nextstep.auth.context.SecurityContextHolder.*;

import javax.servlet.http.HttpServletRequest;

import nextstep.auth.context.SecurityContext;

public class SessionSecurityContextConverter implements SecurityContextConverter {
    public SecurityContext convert(HttpServletRequest request) {
        return (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
    }
}
