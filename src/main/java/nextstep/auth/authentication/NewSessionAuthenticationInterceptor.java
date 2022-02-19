package nextstep.auth.authentication;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;

public class NewSessionAuthenticationInterceptor extends AuthenticationInterceptor {

    private final SessionAuthenticationConverter sessionAuthenticationConverter;

    public NewSessionAuthenticationInterceptor(
        UserDetailsService userDetailsService,
        AuthenticationConverter authenticationConverter,
        SessionAuthenticationConverter sessionAuthenticationConverter) {
        super(userDetailsService, authenticationConverter);
        this.sessionAuthenticationConverter = sessionAuthenticationConverter;
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
