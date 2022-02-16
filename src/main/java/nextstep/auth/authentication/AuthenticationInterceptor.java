package nextstep.auth.authentication;

import nextstep.auth.User;
import nextstep.auth.authentication.after.AfterAuthentication;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;


public class AuthenticationInterceptor implements HandlerInterceptor {
    private final AuthenticationConverter authenticationConverter;
    private final UserDetailsService userDetailsService;
    private final AfterAuthentication afterAuthentication;

    public AuthenticationInterceptor(AuthenticationConverter authenticationConverter, UserDetailsService userDetailsService, AfterAuthentication afterAuthentication) {
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
        User userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);

        return new Authentication(userDetails);
    }

    public void checkAuthentication(User userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
