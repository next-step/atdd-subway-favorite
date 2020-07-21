package nextstep.subway.auth.ui.interceptor.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.member.domain.LoginMember;
import nextstep.subway.member.dto.MemberResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenSecurityContextPersistenceInterceptor implements HandlerInterceptor {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final JwtTokenProvider jwtTokenProvider;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if (!isValidToken(token)) {
            return true;
        }

        String payload = jwtTokenProvider.getPayload(token);

        MemberResponse memberResponse = OBJECT_MAPPER.readValue(payload, MemberResponse.class);

        // TODO LoginMember로 들어온다고 가정하다보니까 payload가 유연하지 않음. Session Interceptor에서도 무조건 LoginMember를 넣고 있는데 일괄적으로 변경 필요
        LoginMember principal = new LoginMember(memberResponse.getId(), memberResponse.getEmail(), null, memberResponse.getAge());

        SecurityContextHolder.setContext(new SecurityContext(new Authentication(principal)));

        return true;
    }

    private boolean isValidToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.clearContext();
    }
}
