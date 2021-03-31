package nextstep.subway.auth.ui.session;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.AuthenticatorStrategy;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

@Component
public class SessionAuthenticator implements AuthenticatorStrategy {

    private final HttpSession httpSession;

    public SessionAuthenticator(HttpSession httpSession){
        this.httpSession = httpSession;
    }

    @Override
    public void authenticate(Authentication authentication, HttpServletResponse response) {
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
