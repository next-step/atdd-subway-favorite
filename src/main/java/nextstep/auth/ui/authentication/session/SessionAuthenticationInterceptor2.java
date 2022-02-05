package nextstep.auth.ui.authentication.session;

import nextstep.auth.domain.Authentication;
import nextstep.auth.infrastructure.SecurityContext;
import nextstep.auth.infrastructure.SecurityContextHolder;
import nextstep.auth.ui.authentication.AuthenticationConverter;
import nextstep.auth.ui.authentication.AuthenticationInterceptor;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static nextstep.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionAuthenticationInterceptor2 extends AuthenticationInterceptor {

    public SessionAuthenticationInterceptor2(SessionAuthenticationConverter converter, CustomUserDetailsService userDetailsService) {
        super(converter, userDetailsService);
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
