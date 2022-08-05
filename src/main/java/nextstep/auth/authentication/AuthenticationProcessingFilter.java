package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationProcessingFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var authenticationToken = convert(request);
        var authentication = authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        return false;
    }

    protected abstract AuthenticationToken convert(HttpServletRequest request);

    protected abstract Authentication authenticate(AuthenticationToken authenticationToken);

}
