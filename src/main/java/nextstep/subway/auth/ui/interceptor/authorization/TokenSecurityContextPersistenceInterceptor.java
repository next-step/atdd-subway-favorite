package nextstep.subway.auth.ui.interceptor.authorization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenSecurityContextPersistenceInterceptor implements HandlerInterceptor {
    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws JsonProcessingException {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if (jwtTokenProvider.validateToken(token)) {
            String payload = jwtTokenProvider.getPayload(token);

            LoginMember userDetails = objectMapper.readValue(payload, LoginMember.class);

            SecurityContext context = new SecurityContext(new Authentication(userDetails));
            SecurityContextHolder.setContext(context);
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.clearContext();
    }
}
