package nextstep.subway.auth.ui.interceptor.authorization;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.dto.MemberResponse;
import nextstep.subway.util.ConvertUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenSecurityContextPersistenceInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String accessToken = extractToken(request);
        if (jwtTokenProvider.validateToken(accessToken)) {
            final String payload = jwtTokenProvider.getPayload(accessToken);
            final LoginMember loginMember = convertPayloadToLoginMember(payload);
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

    private LoginMember convertPayloadToLoginMember(String payload) {
        final MemberResponse memberResponse = ConvertUtils.convertJson2Object(payload, MemberResponse.class);
        return new LoginMember(memberResponse.getId(), memberResponse.getEmail(), null, memberResponse.getAge());
    }

    private SecurityContext buildSecurityContext(LoginMember loginMember) {
        final Authentication authentication = new Authentication(loginMember);
        return new SecurityContext(authentication);
    }
}
