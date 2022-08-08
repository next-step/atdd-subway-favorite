package nextstep.auth.authentication.nonchain;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationNonChainFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authenticate = authentication(authenticationToken);
        afterProcessing(authenticate, response);
        return false;
    }

    public abstract AuthenticationToken convert(HttpServletRequest request) throws Exception;
    public abstract Authentication authentication(AuthenticationToken authenticationToken);
    public abstract void afterProcessing(Authentication authenticate, HttpServletResponse response) throws Exception;
}
