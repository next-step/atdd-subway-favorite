package nextstep.auth.authentication.filter.nonChain;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class NonChainInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            AuthenticationToken authenticationToken = getAuthenticationToken(request);
            Authentication authentication = getAuthentication(authenticationToken);
            doAuthentication(authentication, response);
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    protected abstract AuthenticationToken getAuthenticationToken(HttpServletRequest request) throws IOException;

    protected abstract Authentication getAuthentication(AuthenticationToken authenticationToken);

    protected abstract void doAuthentication(Authentication authentication, HttpServletResponse response) throws IOException;

}
