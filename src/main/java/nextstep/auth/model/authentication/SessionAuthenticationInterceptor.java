package nextstep.auth.model.authentication;

import nextstep.auth.model.authentication.service.CustomUserDetailsService;
import nextstep.auth.model.context.Authentication;
import nextstep.auth.model.context.SecurityContext;
import nextstep.subway.domain.member.MemberAdaptor;
import nextstep.utils.exception.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;

import static nextstep.auth.model.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

@Component(value = "sessionAuthenticationInterceptor")
public class SessionAuthenticationInterceptor implements AuthenticationInterceptor {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    private CustomUserDetailsService userDetailsService;

    public SessionAuthenticationInterceptor(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String principal = paramMap.get(USERNAME_FIELD)[0];
        String credentials = paramMap.get(PASSWORD_FIELD)[0];

        return new AuthenticationToken(principal, credentials);
    }

    @Override
    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getEmail();
        MemberAdaptor memberAdaptor = userDetailsService.loadUserByUsername(principal);
        validatePassword(memberAdaptor, token);

        return new Authentication(memberAdaptor);
    }

    @Override
    public void afterAuthenticate(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession session = request.getSession();
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContext.from(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    private void validatePassword(MemberAdaptor memberAdaptor, AuthenticationToken token) {
        if (!memberAdaptor.checkPassword(token.getPassword())) {
            throw new AuthenticationException();
        }
    }
}
