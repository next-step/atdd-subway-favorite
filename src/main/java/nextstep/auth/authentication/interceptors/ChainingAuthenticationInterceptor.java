package nextstep.auth.authentication.interceptors;

import nextstep.auth.authentication.AuthenticateRequest;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.userdetails.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ChainingAuthenticationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
        try {
            final AuthenticateRequest authenticateRequest = createLoginRequest(request);
            final UserDetails userDetails = findUserDetails(authenticateRequest);

            authenticate(authenticateRequest, userDetails);

            afterAuthenticate(userDetails);
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    abstract AuthenticateRequest createLoginRequest(final HttpServletRequest request);

    abstract UserDetails findUserDetails(final AuthenticateRequest authenticateRequest);

    private void authenticate(final AuthenticateRequest authenticateRequest, final UserDetails userDetails) {
        if (!isAuthenticated(authenticateRequest, userDetails)) {
            throw new AuthenticationException();
        }
    }

    abstract boolean isAuthenticated(final AuthenticateRequest authenticateRequest, final UserDetails userDetails);

    private void afterAuthenticate(final UserDetails userDetails) {
        final Authentication authentication = new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
