package nextstep.subway.auth.ui.interceptor.authorization;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.auth.infrastructure.*;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenSecurityContextPersistenceInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws JsonProcessingException {
        String jwtToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        boolean validated = jwtTokenProvider.validateToken(jwtToken);

        if (validated) {
            UserDetails userDetails = new ObjectMapper().readValue(jwtTokenProvider.getPayload(jwtToken), UserDetails.class);
            SecurityContextHolder.setContext(new SecurityContext(new Authentication(userDetails)));
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        SecurityContextHolder.clearContext();
    }
}
