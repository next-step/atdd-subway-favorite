package nextstep.auth.filters;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationRespondingFilter implements HandlerInterceptor {
    protected final UserDetailsService userDetailsService;

    protected AuthenticationRespondingFilter(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = convert(request);
        UserDetails userDetails = validate(authenticationToken);
        authenticate(userDetails, response);
        return false;
    }

    protected abstract AuthenticationToken convert(HttpServletRequest request) throws IOException;

    protected UserDetails validate(AuthenticationToken token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getPrincipal());
        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
        return userDetails;
    }

    protected abstract void authenticate(UserDetails userDetails, HttpServletResponse response) throws IOException;
}
