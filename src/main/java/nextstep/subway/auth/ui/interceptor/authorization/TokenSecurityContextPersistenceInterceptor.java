package nextstep.subway.auth.ui.interceptor.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {

    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if (!jwtTokenProvider.validateToken(token)) {
            return true;
        }

        LoginMember member = getMemberFrom(token);
        SecurityContext securityContext = new SecurityContext(new Authentication(member));
        SecurityContextHolder.setContext(securityContext);

        return true;
    }

    private LoginMember getMemberFrom(String token) throws IOException {
        String payload = jwtTokenProvider.getPayload(token);
        return objectMapper.readValue(payload, LoginMember.class);
    }
}
