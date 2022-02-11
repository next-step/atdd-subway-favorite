package nextstep.auth.authentication;

import nextstep.auth.application.UserDetailsService;
import nextstep.auth.context.Authentication;
import nextstep.auth.domain.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserDetailsService userDetailsService;
    private final AuthenticationConverter converter;

    protected AuthenticationInterceptor(UserDetailsService userDetailsService, AuthenticationConverter converter) {
        this.userDetailsService = userDetailsService;
        this.converter = converter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken token = converter.convert(request);
        Authentication authentication = authenticate(token);

        afterAuthentication(request, response, authentication);
        return false;
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(UserDetails userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
