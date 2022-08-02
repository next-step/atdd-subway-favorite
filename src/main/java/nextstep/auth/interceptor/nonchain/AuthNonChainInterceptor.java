package nextstep.auth.interceptor.nonchain;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.user.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class AuthNonChainInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserDetails userDetails = createAuthentication(request);

        if (userDetails == null) {
            throw new AuthenticationException();
        }

        afterHandle(userDetails, response);
        return false;
    }

    protected abstract UserDetails createAuthentication(HttpServletRequest request);

    protected abstract void afterHandle(UserDetails userDetails, HttpServletResponse response);

}
