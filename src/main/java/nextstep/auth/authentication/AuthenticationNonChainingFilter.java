package nextstep.auth.authentication;

import nextstep.auth.UserDetails;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationNonChainingFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            UserDetails userDetails = convert(request);
            authenticate(userDetails, response);
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    protected abstract UserDetails convert(HttpServletRequest request) throws IOException;

    protected abstract void authenticate(UserDetails userDetails, HttpServletResponse response) throws IOException;
}
