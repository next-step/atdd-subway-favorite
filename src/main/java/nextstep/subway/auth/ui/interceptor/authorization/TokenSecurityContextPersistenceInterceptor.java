package nextstep.subway.auth.ui.interceptor.authorization;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.member.domain.LoginMember;

public class TokenSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
        JsonProcessingException {
        String jwtToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (jwtTokenProvider.validateToken(jwtToken)) {
            LoginMember loginMember = extractLoginMemberFromToken(jwtToken);
            SecurityContext securityContext = new SecurityContext(new Authentication(loginMember));
            SecurityContextHolder.setContext(securityContext);
        }
        return true;
    }

    private LoginMember extractLoginMemberFromToken(String jwtToken) throws JsonProcessingException {
        String payload = jwtTokenProvider.getPayload(jwtToken);
        return new ObjectMapper().readValue(payload, LoginMember.class);
    }
}
