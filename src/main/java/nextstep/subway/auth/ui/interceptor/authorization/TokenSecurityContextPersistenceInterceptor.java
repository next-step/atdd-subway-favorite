package nextstep.subway.auth.ui.interceptor.authorization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.application.UserDetailService;
import nextstep.subway.member.domain.LoginMember;

public class TokenSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {

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

    private LoginMember extractLoginMemberFromToken(String jwtToken) {
        String payload = jwtTokenProvider.getPayload(jwtToken);
        return customUserDetailsService.loadUserByUserName(payload);
    }
}
