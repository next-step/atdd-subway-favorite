package nextstep.subway.auth.ui.session;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.dto.TokenResponse;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.auth.ui.AuthencationAfterHandler;
import nextstep.subway.auth.ui.AuthenticationConverter;
import nextstep.subway.auth.ui.TokenAuthenticate;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Map;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionAuthenticationInterceptor extends AuthencationAfterHandler implements HandlerInterceptor {

    private TokenAuthenticate tokenAuthenticate;
    private AuthenticationConverter authenticationConverter;

    public SessionAuthenticationInterceptor(TokenAuthenticate tokenAuthenticate) {
        this.tokenAuthenticate = tokenAuthenticate;
        this.authenticationConverter = new SessionAuthenticationConverter();
    }

    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AuthenticationToken token = authenticationConverter.convert(request);
        Authentication authentication = tokenAuthenticate.authenticate(token);
        if (authentication == null) {
            throw new RuntimeException();
        }
        afterAuthentication(request, response, authentication);
        return false;
    }
}
