package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.member.application.CustomUserDetailsService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


public class AuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationConverter authenticationConverter;
    private final CustomUserDetailsService userDetailsService;
    private final AfterAuthentication afterAuthentication;

    public AuthenticationInterceptor(AuthenticationConverter authenticationConverter, CustomUserDetailsService userDetailsService, AfterAuthentication afterAuthentication) {
        this.authenticationConverter = authenticationConverter;
        this.userDetailsService = userDetailsService;
        this.afterAuthentication = afterAuthentication;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken token = authenticationConverter.convert(request);
        Authentication authentication = authenticate(token);

        afterAuthentication.doAfter(request, response, authentication);
        return false;
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        LoginMember userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);

        return new Authentication(userDetails);
    }

    public void checkAuthentication(LoginMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
