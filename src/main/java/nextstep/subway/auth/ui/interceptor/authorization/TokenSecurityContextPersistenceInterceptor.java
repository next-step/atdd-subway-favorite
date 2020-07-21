package nextstep.subway.auth.ui.interceptor.authorization;

import nextstep.subway.auth.application.UserDetail;
import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.*;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenSecurityContextPersistenceInterceptor implements HandlerInterceptor {

    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenSecurityContextPersistenceInterceptor(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String accessToken = extractToken(request);
        if (jwtTokenProvider.validateToken(accessToken)) {
            final String payload = jwtTokenProvider.getPayload(accessToken);
            final UserDetail loginMember = userDetailsService.convertJsonToUserDetail(payload);
            final SecurityContext securityContext = buildSecurityContext(loginMember);
            SecurityContextHolder.setContext(securityContext);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        SecurityContextHolder.clearContext();
    }

    private String extractToken(HttpServletRequest request) {
        return AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
    }

    private SecurityContext buildSecurityContext(UserDetail loginMember) {
        final Authentication authentication = new Authentication(loginMember);
        return new SecurityContext(authentication);
    }
}
