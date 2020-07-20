package nextstep.subway.auth.ui.interceptor.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenSecurityContextPersistenceInterceptor implements HandlerInterceptor {
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
            LoginMember loginMember = loadUser(token);
            Authentication authentication = new Authentication(loginMember);
            SecurityContextHolder.setContext(new SecurityContext(authentication));
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.clearContext();
    }

    private LoginMember loadUser(String token) {
        String principal = tokenProvider.getPayload(token);
        return userDetailsService.loadUserByUsername(principal);
    }
}
