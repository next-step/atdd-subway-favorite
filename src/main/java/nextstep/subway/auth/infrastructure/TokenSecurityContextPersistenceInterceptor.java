package nextstep.subway.auth.infrastructure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.member.application.UserDetailService;
import nextstep.subway.member.domain.LoginMember;

public class TokenSecurityContextPersistenceInterceptor implements HandlerInterceptor {

    private final UserDetailService customUserDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    public TokenSecurityContextPersistenceInterceptor(UserDetailService customUserDetailsService,
        JwtTokenProvider jwtTokenProvider) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String jwtToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (jwtTokenProvider.validateToken(jwtToken)) {
            LoginMember loginMember = extractLoginMemberFromToken(jwtToken);
            SecurityContext securityContext = new SecurityContext(new Authentication(loginMember));
            SecurityContextHolder.setContext(securityContext);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
        Exception exception) {
        SecurityContextHolder.clearContext();
    }

    private LoginMember extractLoginMemberFromToken(String jwtToken) {
        String payload = jwtTokenProvider.getPayload(jwtToken);
        return customUserDetailsService.loadUserByUserName(payload);
    }
}
