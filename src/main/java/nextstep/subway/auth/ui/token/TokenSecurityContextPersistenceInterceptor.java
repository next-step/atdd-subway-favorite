package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.exception.NotValidPassword;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class TokenSecurityContextPersistenceInterceptor implements HandlerInterceptor {

    private final CustomUserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    public TokenSecurityContextPersistenceInterceptor(
        CustomUserDetailsService userDetailsService,
        JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return true;
        }

        String credentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (!jwtTokenProvider.validateToken(credentials)) {
            return true;
        }

        SecurityContext securityContext = extractSecurityContext(credentials);
        if (securityContext != null) {
            final LoginMember loginMember =
                objectMapper.convertValue(securityContext.getAuthentication().getPrincipal(), LoginMember.class);

            if (!userDetailsService.loadUserByUsername(loginMember.getEmail())
                .checkPassword(loginMember.getPassword())) {
                throw new NotValidPassword();
            }
            SecurityContextHolder.setContext(securityContext);
        }
        return true;
    }

    private SecurityContext extractSecurityContext(String credentials) {
        try {
            String payload = jwtTokenProvider.getPayload(credentials);
            TypeReference<Map<String, String>> typeRef = new TypeReference<Map<String, String>>() {
            };

            Map principal = objectMapper.readValue(payload, typeRef);
            return new SecurityContext(new Authentication(principal));
        } catch (Exception e) {
            return null;
        }
    }
}
