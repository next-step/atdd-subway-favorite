package nextstep.auth.authorization;

import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityContextHolderStrategy strategy;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider, SecurityContextHolderStrategy strategy) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.strategy = strategy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return true;
        }

        SecurityContext securityContext = strategy.extract(request);
        strategy.setContext(securityContext);
        return true;
    }
}
