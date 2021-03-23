package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.auth.ui.SecurityContextInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TokenSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {

    private JwtTokenProvider jwtTokenProvider;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void setSecurityContext(HttpServletRequest request) {
        String credentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (!jwtTokenProvider.validateToken(credentials)) {
            return;
        }

        SecurityContext securityContext = extractSecurityContext(credentials);
        if (securityContext != null) {
            SecurityContextHolder.setContext(securityContext);
        }
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
}
