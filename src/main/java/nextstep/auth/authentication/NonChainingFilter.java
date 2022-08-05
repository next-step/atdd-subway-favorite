package nextstep.auth.authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class NonChainingFilter implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {


            AuthenticationToken token = convert(request);
            Authentication authentication = authenticate(token);
            afterAuthenticated(authentication, response);
            return false;
        } catch (Exception e) {
            return true;
        }

    }

    public abstract AuthenticationToken convert(HttpServletRequest request) throws Exception;

    public abstract Authentication authenticate(AuthenticationToken token);

    public abstract void afterAuthenticated(Authentication authentication, HttpServletResponse response) throws JsonProcessingException, Exception;

}
