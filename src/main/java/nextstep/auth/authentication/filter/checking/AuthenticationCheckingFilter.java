package nextstep.auth.authentication.filter.checking;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationCheckingFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            var authenticationToken = convert(request);
            var authentication = authenticate(authenticationToken);

            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            return true;
        }
        return true;
    }

    protected abstract AuthenticationToken convert(HttpServletRequest request) throws IOException;

    protected abstract Authentication authenticate(AuthenticationToken authenticationToken);
}
