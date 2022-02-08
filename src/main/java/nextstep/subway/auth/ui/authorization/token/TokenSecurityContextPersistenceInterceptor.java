package nextstep.subway.auth.ui.authorization.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.auth.ui.authorization.SecurityContextInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TokenSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {

    private JwtTokenProvider jwtTokenProvider;
    private ObjectMapper objectMapper;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    protected SecurityContext extractSecurityContext(HttpServletRequest request) {
        String credentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (!jwtTokenProvider.validateToken(credentials)) {
            return null;
        }

        SecurityContext securityContext = extractSecurityContext(credentials);
        if (securityContext != null) {
            SecurityContextHolder.setContext(securityContext);
        }

        return null;
    }

    private SecurityContext extractSecurityContext(String credentials) {
        try {
            String payload = jwtTokenProvider.getPayload(credentials);
            TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
            };

            Map<String, String> principal = objectMapper.readValue(payload, typeRef);
            return new SecurityContext(new Authentication(principal));
        } catch (Exception e) {
            return null;
        }
    }
}
