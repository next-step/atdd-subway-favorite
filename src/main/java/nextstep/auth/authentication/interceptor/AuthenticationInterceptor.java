package nextstep.auth.authentication.interceptor;

import nextstep.auth.application.UserDetailService;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.converter.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.domain.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationConverter authenticationConverter;
    private final UserDetailService userDetailService;

    public AuthenticationInterceptor(AuthenticationConverter authenticationConverter, UserDetailService userDetailService) {
        this.authenticationConverter = authenticationConverter;
        this.userDetailService = userDetailService;
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);
        afterAuthentication(request, response, authentication);
        return false;
    }

    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        return authenticationConverter.convert(request);
    }

    public Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        UserDetails userDetails = userDetailService.loadUserByUsername(principal);
        checkAuthentication(userDetails, authenticationToken);

        return new Authentication(userDetails);
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

    private void checkAuthentication(UserDetails userDetails, AuthenticationToken authenticationToken) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(authenticationToken.getCredentials())) {
            throw new AuthenticationException();
        }
    }


}
