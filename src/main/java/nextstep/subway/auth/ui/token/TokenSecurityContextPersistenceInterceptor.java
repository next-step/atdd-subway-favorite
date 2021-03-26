package nextstep.subway.auth.ui.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.UserDetails;
import nextstep.subway.auth.infrastructure.*;
import nextstep.subway.auth.ui.LoginMemberPort;
import nextstep.subway.auth.ui.SecurityContextInterceptor;
import nextstep.subway.member.domain.CustomUserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenSecurityContextPersistenceInterceptor extends SecurityContextInterceptor {
    private JwtTokenProvider jwtTokenProvider;
    private LoginMemberPort loginMemberPort;

    public TokenSecurityContextPersistenceInterceptor(JwtTokenProvider jwtTokenProvider, LoginMemberPort loginMemberPort) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.loginMemberPort = loginMemberPort;
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
            UserDetails userDetails = loginMemberPort.getUserDetailFromPayload(payload);
            return new SecurityContext(new Authentication(userDetails));
        } catch (Exception e) {
            return null;
        }
    }
}
