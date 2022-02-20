package nextstep.auth.authentication.interceptor;

import nextstep.auth.authentication.convertor.SessionConvertor;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

@Component
public class SessionAuthenticationInterceptor extends AuthenticationInterceptor {

    public SessionAuthenticationInterceptor(CustomUserDetailsService userDetailsService) {
        super(userDetailsService, new SessionConvertor());
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
    }

}
