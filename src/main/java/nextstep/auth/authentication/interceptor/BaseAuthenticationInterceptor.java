package nextstep.auth.authentication.interceptor;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.convert.AuthenticationConverter;
import nextstep.auth.context.Authentication;
import nextstep.auth.manager.UserDetailsService;
import nextstep.auth.manager.UserMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseAuthenticationInterceptor implements HandlerInterceptor {

    private UserDetailsService userDetailsService;
    private AuthenticationConverter authenticationConverter;

    public BaseAuthenticationInterceptor(UserDetailsService userDetailsService, AuthenticationConverter authenticationConverter) {
        this.userDetailsService = userDetailsService;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken token = convert(request);
        Authentication authentication = authenticate(token);

        afterAuthentication(request, response, authentication);
        return false;
    }

    // test용
    public AuthenticationToken convert(HttpServletRequest request) throws IOException {
        return authenticationConverter.convert(request);
    }

    private Authentication authenticate(AuthenticationToken authenticationToken) {
        String principal = authenticationToken.getPrincipal();
        UserMember userMember = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userMember, authenticationToken);

        return new Authentication(userMember);
    }

    public abstract void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;

    private void checkAuthentication(UserMember userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }
        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
