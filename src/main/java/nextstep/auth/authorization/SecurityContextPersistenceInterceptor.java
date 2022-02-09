package nextstep.auth.authorization;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.auth.authorization.converter.SecurityContextConverter;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;

public class SecurityContextPersistenceInterceptor implements HandlerInterceptor {
    private final List<SecurityContextConverter> securityContextConverters;

    public SecurityContextPersistenceInterceptor(List<SecurityContextConverter> securityContextConverters) {
        this.securityContextConverters = securityContextConverters;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return true;
        }

        securityContext(request).ifPresent(SecurityContextHolder::setContext);
        return true;
    }

    private Optional<SecurityContext> securityContext(HttpServletRequest request) {
        return securityContextConverters.stream()
            .map(eachConverter -> eachConverter.convert(request))
            .filter(Objects::nonNull)
            .findFirst();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.clearContext();
    }
}
