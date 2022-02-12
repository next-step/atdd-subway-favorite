package nextstep.auth.model.authentication;

import nextstep.auth.model.context.Authentication;
import nextstep.auth.model.context.SecurityContext;
import nextstep.auth.model.authentication.service.CustomUserDetailsService;
import nextstep.subway.domain.member.MemberAdaptor;
import nextstep.utils.exception.AuthenticationException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static nextstep.auth.model.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionAuthenticationInterceptor implements HandlerInterceptor {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    private CustomUserDetailsService userDetailsService;

    public SessionAuthenticationInterceptor(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AuthenticationToken token = convert(request);
        Authentication authentication = authenticate(token);

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String principal = paramMap.get(USERNAME_FIELD)[0];
        String credentials = paramMap.get(PASSWORD_FIELD)[0];

        return new AuthenticationToken(principal, credentials);
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getEmail();
        MemberAdaptor memberAdaptor = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(memberAdaptor, token);

        return new Authentication(memberAdaptor);
    }

    private void checkAuthentication(MemberAdaptor memberAdaptor, AuthenticationToken token) {
        if (memberAdaptor == null) {
            throw new AuthenticationException();
        }

        if (!memberAdaptor.checkPassword(token.getPassword())) {
            throw new AuthenticationException();
        }
    }
}
