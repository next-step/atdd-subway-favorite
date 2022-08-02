package nextstep.auth.interceptor.nonchain;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.user.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

abstract class AuthNonChainInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        UserDetails userDetails = createAuthentication(request);

        if (userDetails == null) {
            throw new AuthenticationException();
        }

        afterHandle(userDetails, response);
        return false;
    }

    abstract UserDetails createAuthentication(HttpServletRequest request);
    abstract void afterHandle(UserDetails userDetails, HttpServletResponse response);

}
