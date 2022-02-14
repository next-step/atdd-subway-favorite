package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private UserDetailService customUserDetailsService;
    private AuthenticationConverter authenticationConverter;

    public AuthenticationInterceptor(UserDetailService customUserDetailsService, AuthenticationConverter authenticationConverter) {
        this.customUserDetailsService = customUserDetailsService;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = authenticationConverter.convert(request);
        Authentication authentication = authenticate(authenticationToken);

        afterAuthentication(request, response, authentication);

        return false;
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

    private Authentication authenticate(AuthenticationToken authenticationToken) {
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(authenticationToken.getPrincipal());

        checkAuthentication(userDetails, authenticationToken);

        return new Authentication(userDetails);
    }

    private void checkAuthentication(UserDetails userDetails, AuthenticationToken authenticationToken) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
