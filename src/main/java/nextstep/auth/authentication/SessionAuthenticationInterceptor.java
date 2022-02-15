package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.member.application.CustomUserDetailServiceStrategy;
import nextstep.member.application.CustomUserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionAuthenticationInterceptor implements HandlerInterceptor, AuthenticationConverter {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    private CustomUserDetailsService userDetailsService;

    public SessionAuthenticationInterceptor(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AuthenticationToken token = convert(request);
        Authentication authentication = authenticate(token, new CustomUserDetailServiceStrategy(userDetailsService));

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
        return false;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String principal = paramMap.get(USERNAME_FIELD)[0];
        String credentials = paramMap.get(PASSWORD_FIELD)[0];

        return new AuthenticationToken(principal, credentials);
    }
}
