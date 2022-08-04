package nextstep.auth.authentication;

import nextstep.auth.UserDetails;
import nextstep.auth.UserDetailsService;
import nextstep.auth.token.TokenRequest;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationNonChainingFilter implements HandlerInterceptor {

    protected final UserDetailsService userDetailsService;

    protected AuthenticationNonChainingFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            TokenRequest tokenRequest = convert(request);
            UserDetails userDetails = userDetailsService.loadUserByUsername(tokenRequest.getEmail());
            validateUserDetails(userDetails, tokenRequest.getPassword());
            authenticate(userDetails, response);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    protected abstract TokenRequest convert(HttpServletRequest request) throws IOException;

    private void validateUserDetails(UserDetails userDetails, String credentials) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(credentials)) {
            throw new AuthenticationException();
        }
    }

    protected abstract void authenticate(UserDetails userDetails, HttpServletResponse response) throws IOException;
}
