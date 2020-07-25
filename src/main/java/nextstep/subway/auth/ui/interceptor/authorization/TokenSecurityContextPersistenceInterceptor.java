package nextstep.subway.auth.ui.interceptor.authorization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws JsonProcessingException {
        String token = extractToken(request);

        if (jwtTokenProvider.validateToken(token)) {
            LoginMember loginMember = getLoginMemberFromToken(token);
            SecurityContext securityContext = new SecurityContext(new Authentication(loginMember));
            SecurityContextHolder.setContext(securityContext);
        }

        return true;
    }

    private String extractToken(HttpServletRequest request) {
        return AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
    }

    private LoginMember getLoginMemberFromToken(String token) throws JsonProcessingException {
        String payload = jwtTokenProvider.getPayload(token);
        return objectMapper.readValue(payload, LoginMember.class);
    }
}
