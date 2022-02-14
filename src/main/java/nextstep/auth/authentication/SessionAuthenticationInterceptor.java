package nextstep.auth.authentication;

import nextstep.auth.authentication.application.UserDetailsService;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionAuthenticationInterceptor extends AuthenticationInterceptor {
    public SessionAuthenticationInterceptor(
            UserDetailsService userDetailsService,
            AuthenticationConverter authenticationConverter
    ) {
        super(userDetailsService, authenticationConverter);
    }

    @Override
    public void afterAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler,
            Exception ex
    ) {
        SecurityContextHolder.clearContext();
    }
}
