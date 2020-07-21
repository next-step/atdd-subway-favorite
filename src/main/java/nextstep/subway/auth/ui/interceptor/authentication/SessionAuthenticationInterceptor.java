package nextstep.subway.auth.ui.interceptor.authentication;

import nextstep.subway.auth.application.converter.AuthenticationConverter;
import nextstep.subway.auth.domain.Authentication;
import nextstep.subway.auth.domain.AuthenticationToken;
import nextstep.subway.auth.infrastructure.SecurityContext;
import nextstep.subway.member.application.CustomUserDetailsService;
import nextstep.subway.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static nextstep.subway.auth.infrastructure.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionAuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationConverter authenticationConverter;
    private final CustomUserDetailsService userDetailsService;

    public SessionAuthenticationInterceptor(AuthenticationConverter authenticationConverter, CustomUserDetailsService userDetailsService) {
        this.authenticationConverter = authenticationConverter;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        AuthenticationToken token = authenticationConverter.convert(request);
        Authentication authentication = authenticate(token);

        if (authentication == null) {
            throw new RuntimeException();
        }

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
        return false;
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        LoginMember userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new RuntimeException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new RuntimeException();
        }
    }
}
