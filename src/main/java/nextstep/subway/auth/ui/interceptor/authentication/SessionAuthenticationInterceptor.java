package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.member.application.CustomUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionAuthenticationInterceptor extends AbstractAuthenticationInterceptor {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    public SessionAuthenticationInterceptor(CustomUserDetailsService userDetailsService) {
        super(userDetailsService);
    }

    @Override
    public void applyAuthentication(HttpServletRequest request, HttpServletResponse response) {
        AuthenticationToken token = convert(request);
        Authentication authentication = authenticate(token);

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
    }

    public AuthenticationToken convert(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String principal = paramMap.get(USERNAME_FIELD)[0];
        String credentials = paramMap.get(PASSWORD_FIELD)[0];

        return new AuthenticationToken(principal, credentials);
    }
}
