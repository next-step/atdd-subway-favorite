package nextstep.auth.authentication;

import nextstep.auth.user.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class InterceptorNoChainingFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            UserDetails userDetails = getUserDetails(request);
            setAuthentication(userDetails, response);
        } catch (AuthenticationException e) {

        } catch (IOException e) {

        }

        return false;
    }

    protected abstract UserDetails getUserDetails(HttpServletRequest request) throws IOException;

    protected abstract void setAuthentication(UserDetails userDetails, HttpServletResponse response) throws IOException;
}
