package nextstep.auth.authorization;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {
    private JwtTokenProvider jwtTokenProvider;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            return true;
        }

        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (!jwtTokenProvider.validateToken(token)) {
            return true;
        }

        SecurityContext securityContext = extractSecurityContext(token);
        if (securityContext != null) {
            SecurityContextHolder.setContext(securityContext);
        }
        return true;
    }

    private SecurityContext extractSecurityContext(String token) {
        try {
            String payload = jwtTokenProvider.getPayload(token);

            LoginMember loginMember = new ObjectMapper().readValue(payload, LoginMember.class);
            return new SecurityContext(new Authentication(loginMember));
        } catch (Exception e) {
            return null;
        }
    }
}
