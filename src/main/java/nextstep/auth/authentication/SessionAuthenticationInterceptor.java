package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.member.application.CustomUserDetailServiceStrategy;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.application.UserDetailsServiceStrategy;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionAuthenticationInterceptor implements HandlerInterceptor {


    private CustomUserDetailsService userDetailsService;
    private AuthenticationConverter authenticationConverter;

    public SessionAuthenticationInterceptor(CustomUserDetailsService userDetailsService, AuthenticationConverter authenticationConverter) {
        this.userDetailsService = userDetailsService;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken token = authenticationConverter.convert(request);
        Authentication authentication = authenticate(token, new CustomUserDetailServiceStrategy(userDetailsService));

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));
        response.setStatus(HttpServletResponse.SC_OK);
        return false;
    }

    Authentication authenticate(AuthenticationToken token, UserDetailsServiceStrategy userDetailsService) {
        String principal = token.getPrincipal();
        final LoginMember userDetails = userDetailsService.authenticate(principal, token);
        return new Authentication(userDetails);
    }
}
