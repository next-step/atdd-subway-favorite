package nextstep.auth.authentication;

import nextstep.auth.user.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class InterceptorChainingFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            UserDetails userDetails = getUserDetails(request);
            setAuthentication(userDetails);
        } catch (AuthenticationException e) {
            // ?
        } catch (IndexOutOfBoundsException ignore) {
            // BasicAuthenticationFilter에서만 발생. Bearer 인증으로 들어온다.
        }

        return true;
    }

    protected abstract UserDetails getUserDetails(HttpServletRequest request);

    protected abstract void setAuthentication(UserDetails userDetails);
}
