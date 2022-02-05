package nextstep.auth.authentication;

import nextstep.auth.authentication.convert.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.service.UserDetailsService;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

import static nextstep.auth.authentication.AuthenticationException.NOT_FOUND_EMAIL;
import static nextstep.auth.authentication.AuthenticationException.PASSWORD_IS_INCORRECT;
import static nextstep.auth.context.SecurityContextHolder.SPRING_SECURITY_CONTEXT_KEY;

public class SessionAuthenticationInterceptor extends AuthenticationInterceptor {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    private final UserDetailsService userDetailsService;
    private final AuthenticationConverter sessionAuthenticationConverter;

    public SessionAuthenticationInterceptor(UserDetailsService userDetailsService, AuthenticationConverter sessionAuthenticationConverter) {
        this.userDetailsService = userDetailsService;
        this.sessionAuthenticationConverter = sessionAuthenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken token = sessionAuthenticationConverter.convert(request);
        Authentication authentication = authenticate(token);
        afterAuthentication(request, response, authentication);
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
            throw new AuthenticationException(NOT_FOUND_EMAIL);
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException(PASSWORD_IS_INCORRECT);
        }
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        HttpSession httpSession = request.getSession();
        httpSession.setAttribute(SPRING_SECURITY_CONTEXT_KEY, new SecurityContext(authentication));

        response.setStatus(HttpServletResponse.SC_OK);
    }
}
