package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class Authorizator implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        try {
            Authentication authentication = convert(request);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (RuntimeException e){
            return true;
        }

        return true;
    }

    abstract public Authentication convert(HttpServletRequest request);
}
