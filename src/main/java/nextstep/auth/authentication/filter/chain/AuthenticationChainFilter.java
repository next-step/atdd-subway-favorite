package nextstep.auth.authentication.filter.chain;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationChainFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            AuthenticationToken token = createToken(request);
            Authentication authentication = authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return true;
        } catch (AuthenticationException e) {
            throw new AuthenticationException();
        } catch (Exception e) {
            return true;
        }
    }

    protected abstract AuthenticationToken createToken(HttpServletRequest request);

    protected abstract Authentication authenticate(AuthenticationToken token);

}
