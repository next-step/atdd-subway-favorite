package nextstep.auth.model.authorization;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.model.authentication.service.CustomUserDetailsService;
import nextstep.auth.model.context.Authentication;
import nextstep.auth.model.context.SecurityContext;
import nextstep.auth.model.context.SecurityContextHolder;
import nextstep.auth.model.token.JwtTokenProvider;
import nextstep.subway.domain.member.MemberAdaptor;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class TokenSecurityContextPersistenceInterceptor implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider, CustomUserDetailsService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return true;
        }

        /* request 에 담긴 토큰 유효성 검증*/
        String jwtToken = extractJwtToken(request);
        if (!jwtTokenProvider.validateToken(jwtToken)) {
            return true;
        }

        /* 토큰에 담긴 정보에 대한 유효성 검증 */
        SecurityContext securityContext = extractSecurityContext(jwtToken);
        if (securityContext != null) {
            SecurityContextHolder.setContext(securityContext);
        }

        return true;
    }

    private String extractJwtToken(HttpServletRequest request) {
        return AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
    }

    private SecurityContext extractSecurityContext(String jwtToken) {
        try {
            String email = jwtTokenProvider.getPayload(jwtToken);
            MemberAdaptor memberAdaptor = userDetailsService.loadUserByUsername(email);

            return new SecurityContext();
        } catch (Exception e) {
            return null;
        }
    }
}
