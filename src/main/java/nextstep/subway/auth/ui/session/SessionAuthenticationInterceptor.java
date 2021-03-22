package nextstep.subway.auth.ui.session;

import nextstep.subway.auth.application.UserDetailsService;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.AuthenticationInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionAuthenticationInterceptor extends AuthenticationInterceptor {

    public SessionAuthenticationInterceptor(UserDetailsService userDetailsService) {
        super(userDetailsService, new SessionAuthenticationConverter());
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication == null) {
            throw new RuntimeException();
        }

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
