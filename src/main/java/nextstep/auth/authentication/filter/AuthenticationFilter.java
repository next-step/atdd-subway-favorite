package nextstep.auth.authentication.filter;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            var authenticationToken = convert(request);
            var authentication = authenticate(authenticationToken);

            processing(authentication, response);

        } catch (Exception e) {
            return proceed();
        }
        return proceed();
    }

    protected abstract AuthenticationToken convert(HttpServletRequest request) throws IOException;

    protected abstract Authentication authenticate(AuthenticationToken authenticationToken);

    protected abstract void processing(Authentication authentication, HttpServletResponse response) throws Exception;

    protected abstract boolean proceed();

}
