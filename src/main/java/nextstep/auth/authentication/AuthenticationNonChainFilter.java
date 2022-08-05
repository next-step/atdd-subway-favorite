package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationNonChainFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken token = createToken(request);
        Authentication authenticate = authenticate(token);

        return send(authenticate, response);
    }

    protected abstract AuthenticationToken createToken(HttpServletRequest request) throws IOException;

    protected abstract Authentication authenticate(AuthenticationToken token);

    protected abstract boolean send(Authentication authentication, HttpServletResponse response) throws IOException;
}
