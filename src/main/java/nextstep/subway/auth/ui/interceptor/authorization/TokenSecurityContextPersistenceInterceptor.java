package nextstep.subway.auth.ui.interceptor.authorization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class TokenSecurityContextPersistenceInterceptor  implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws JsonProcessingException {
        String token = extractToken(request);
        LoginMember loginMember = getLoginMemberFromToken(token);

        SecurityContext securityContext = (SecurityContext) request.getSession().getAttribute(SPRING_SECURITY_CONTEXT_KEY);
        if (securityContext != null) {
            SecurityContextHolder.setContext(securityContext);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.clearContext();
    }

    private String extractToken(HttpServletRequest request) {
        return AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
    }

    private LoginMember getLoginMemberFromToken(String token) throws JsonProcessingException {
        return objectMapper.readValue(jwtTokenProvider.getPayload(token), LoginMember.class);
    }

}
