package nextstep.auth.intercpetor;

import com.fasterxml.jackson.core.JsonProcessingException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class NonChainFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            AuthenticationToken token = convert(request);
            Authentication authentication = authenticate(token);
            afterAuthenticated(authentication, response);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    public abstract AuthenticationToken convert(HttpServletRequest request) throws Exception;

    public abstract Authentication authenticate(AuthenticationToken token);

    public abstract void afterAuthenticated(Authentication authentication, HttpServletResponse response) throws JsonProcessingException, Exception;
}
