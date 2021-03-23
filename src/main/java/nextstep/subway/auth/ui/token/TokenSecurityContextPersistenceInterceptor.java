package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.auth.ui.AbstractSecurityContextPersistenceInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class TokenSecurityContextPersistenceInterceptor extends AbstractSecurityContextPersistenceInterceptor {
    private JwtTokenProvider jwtTokenProvider;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return true;
        }

        String credentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (!jwtTokenProvider.validateToken(credentials)) {
            return true;
        }

        SecurityContext securityContext = extractSecurityContext(credentials);
        if (securityContext != null) {
            SecurityContextHolder.setContext(securityContext);
        }
        return true;
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
