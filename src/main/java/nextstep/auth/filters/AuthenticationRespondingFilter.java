package nextstep.auth.filters;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.filters.provider.AuthenticationProvider;
import nextstep.auth.user.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationRespondingFilter implements HandlerInterceptor {
    private final AuthenticationProvider<AuthenticationToken> authenticationProvider;

    protected AuthenticationRespondingFilter(AuthenticationProvider<AuthenticationToken> authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = convert(request);
        UserDetails userDetails = authenticationProvider.provide(authenticationToken);
        authenticate(userDetails, response);
        return false;
    }

    protected abstract AuthenticationToken convert(HttpServletRequest request) throws IOException;

    protected abstract void authenticate(UserDetails userDetails, HttpServletResponse response) throws IOException;
}
