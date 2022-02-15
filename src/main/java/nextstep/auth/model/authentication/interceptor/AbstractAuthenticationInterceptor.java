package nextstep.auth.model.authentication.interceptor;

import nextstep.auth.model.authentication.AuthenticationToken;
import nextstep.auth.model.context.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractAuthenticationInterceptor implements AuthenticationInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);
        afterAuthenticate(request, response, authentication);

        return false;
    }

    protected abstract AuthenticationToken convert(HttpServletRequest request) throws IOException;

    protected abstract Authentication authenticate(AuthenticationToken authenticationToken);

    protected abstract void afterAuthenticate(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;
}
