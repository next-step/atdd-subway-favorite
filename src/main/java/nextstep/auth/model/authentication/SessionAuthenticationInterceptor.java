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
        return null;
    }

    @Override
    public Authentication authenticate(AuthenticationToken token) {
        return null;
    }

    @Override
    public void afterAuthenticate(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
    }

    private void validatePassword(MemberAdaptor memberAdaptor, AuthenticationToken token) {
        if (!memberAdaptor.checkPassword(token.getPassword())) {
            throw new AuthenticationException();
        }
    }
}
