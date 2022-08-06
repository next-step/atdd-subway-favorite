package nextstep.auth.intercpetor;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ChainFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            AuthenticationToken token = convert(request);
            Authentication authentication = authenticate(token);

            SecurityContextHolder.getContext()
                    .setAuthentication(authentication);
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public abstract AuthenticationToken convert(HttpServletRequest request);

    public abstract Authentication authenticate(AuthenticationToken token);
}