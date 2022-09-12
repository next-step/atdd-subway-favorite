package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthContextChainInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler){
        try {
            AuthenticationToken token = createAuthToken(request);
            checkValidAuth(token);
            saveAuthContext(token);
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    protected abstract void checkValidAuth(final AuthenticationToken token);

    protected abstract Authentication getAuthentication(final AuthenticationToken token);

    protected abstract AuthenticationToken createAuthToken(final HttpServletRequest request);

    private void saveAuthContext(final AuthenticationToken token) {
        Authentication authentication = getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
