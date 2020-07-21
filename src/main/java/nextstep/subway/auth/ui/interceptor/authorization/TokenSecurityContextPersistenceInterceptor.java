package nextstep.subway.auth.ui.interceptor.authorization;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.User;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.member.application.CustomUserDetailsService;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenSecurityContextPersistenceInterceptor extends AbstractSecurityContextPersistenceInterceptor {
    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider tokenProvider;

    public TokenSecurityContextPersistenceInterceptor(CustomUserDetailsService userDetailsService, JwtTokenProvider tokenProvider) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (tokenProvider.validateToken(token)) {
            User user = loadUser(token);
            Authentication authentication = new Authentication(user);
            SecurityContextHolder.setContext(new SecurityContext(authentication));
        }
        return true;
    }

    private User loadUser(String token) {
        String principal = tokenProvider.getPayload(token);
        return userDetailsService.loadUserByUsername(principal);
    }
}
