package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.SecurityContextInterceptor;
import nextstep.subway.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;

public class TokenSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean proceed(HttpServletRequest request) {
        String credentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (!jwtTokenProvider.validateToken(credentials)) {
            return true;
        }

        SecurityContext securityContext = extractSecurityContext(credentials);
        setSecurityContext(securityContext);
        return true;
    }

    private SecurityContext extractSecurityContext(String credentials) {
        try {
            String payload = jwtTokenProvider.getPayload(credentials);
            UserDetails principal = objectMapper.readValue(payload, LoginMember.class);
            Authentication authentication = new Authentication(principal);
            return new SecurityContext(authentication);
        } catch (Exception e) {
            return null;
        }
    }
}
