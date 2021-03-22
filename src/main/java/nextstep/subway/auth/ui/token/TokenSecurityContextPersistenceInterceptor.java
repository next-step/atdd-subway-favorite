package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.AuthorizationExtractor;
import nextstep.subway.auth.infrastructure.AuthorizationType;
import nextstep.subway.auth.infrastructure.JwtTokenProvider;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.base.SecurityContextInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TokenSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    private SecurityContext extractSecurityContext(String credentials) {
        try {
            String payload = jwtTokenProvider.getPayload(credentials);
            TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
            };

            Map principal = new ObjectMapper().readValue(payload, typeRef);
            return new SecurityContext(new Authentication(principal));
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public SecurityContext getSecurityContext(HttpServletRequest request) {
        String credentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (!jwtTokenProvider.validateToken(credentials)) {
            return null;
        }

        return extractSecurityContext(credentials);
    }
}
