package nextstep.subway.auth.ui.session;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.infrastructure.SecurityContextHolder;
import nextstep.subway.auth.ui.auth.interceptor.AuthenticationInterceptor;
import nextstep.subway.auth.ui.auth.converter.SessionAuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class SessionAuthenticationInterceptor2 extends AuthenticationInterceptor {

    public SessionAuthenticationInterceptor2(UserDetailsService userDetailsService) {
        super(userDetailsService, new SessionAuthenticationConverter());
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication == null) {
            throw new RuntimeException("Authentication이 생성되지 않았습니다.");
        }

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
