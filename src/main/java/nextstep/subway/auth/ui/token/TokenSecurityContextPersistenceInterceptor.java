package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.User;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.auth.ui.SecurityContextPersistenceInterceptor;
import nextstep.subway.exceptions.UnMatchedPasswordException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class TokenSecurityContextPersistenceInterceptor extends SecurityContextPersistenceInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailService customUserDetailsService;
    private final ObjectMapper objectMapper;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider, UserDetailService customUserDetailsService, ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.customUserDetailsService = customUserDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected boolean proceedAfter(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String credentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (!jwtTokenProvider.validateToken(credentials)) {
            return true;
        }

        SecurityContext securityContext = extractSecurityContext(credentials);
        if (securityContext != null) {
            checkSecurityContextValidation(securityContext);
            SecurityContextHolder.setContext(securityContext);
        }

        return true;
    }

    private void checkSecurityContextValidation(SecurityContext securityContext) {
        User user = objectMapper.convertValue(securityContext.getAuthentication().getPrincipal(), User.class);
        User foundMember = customUserDetailsService.loadUserByUsername(user.getUsername());

        if (!foundMember.checkPassword(user.getPassword())) {
            throw new UnMatchedPasswordException();
        }

    }

    private SecurityContext extractSecurityContext(String credentials) {
        try {
            String payload = jwtTokenProvider.getPayload(credentials);
            TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {
            };

            Map principal = objectMapper.readValue(payload, typeRef);
            return new SecurityContext(new Authentication(principal));
        } catch (Exception e) {
            return null;
        }
    }
}
