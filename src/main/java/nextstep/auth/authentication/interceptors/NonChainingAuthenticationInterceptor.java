package nextstep.auth.authentication.interceptors;

import nextstep.auth.authentication.AuthenticateRequest;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.userdetails.UserDetails;
import nextstep.auth.userdetails.UserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class NonChainingAuthenticationInterceptor implements HandlerInterceptor {

    private final UserDetailsService userDetailsService;

    public NonChainingAuthenticationInterceptor(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) throws Exception {
        final AuthenticateRequest authenticateRequest = createLoginRequest(request);
        final UserDetails userDetails = findMember(authenticateRequest.getEmail());

        authenticate(authenticateRequest, userDetails);

        afterAuthenticate(response, userDetails);
        return false;
    }

    abstract AuthenticateRequest createLoginRequest(final HttpServletRequest request) throws IOException;

    private UserDetails findMember(final String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        if (userDetails == null) {
            throw new AuthenticationException();
        }
        return userDetails;
    }

    private void authenticate(final AuthenticateRequest authenticateRequest, final UserDetails loginMember) {
        if (!loginMember.checkPassword(authenticateRequest.getPassword())) {
            throw new AuthenticationException();
        }
    }

    abstract void afterAuthenticate(final HttpServletResponse response, final UserDetails loginMember) throws IOException;

}
