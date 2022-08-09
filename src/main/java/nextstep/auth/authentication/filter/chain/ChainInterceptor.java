package nextstep.auth.authentication.filter.chain;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ChainInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            AuthenticationToken authenticationToken = getAuthenticationToken(request);
            Authentication authentication = getAuthentication(authenticationToken);
            doAuthentication(authentication, response);
        } catch (Exception e) {
            return true;
        }
        return true;
    }

    protected abstract AuthenticationToken getAuthenticationToken(HttpServletRequest request);

    protected abstract Authentication getAuthentication(AuthenticationToken authenticationToken);

    protected abstract void doAuthentication(Authentication authentication, HttpServletResponse response);

}
