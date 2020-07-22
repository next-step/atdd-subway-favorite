package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.interceptor.authentication.converter.AuthenticationConverter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionAuthenticationInterceptor extends AuthenticationInterceptor {

    public SessionAuthenticationInterceptor(UserDetailsService userDetailsService,
                                            AuthenticationConverter authenticationConverter) {
        super(userDetailsService, authenticationConverter);
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
