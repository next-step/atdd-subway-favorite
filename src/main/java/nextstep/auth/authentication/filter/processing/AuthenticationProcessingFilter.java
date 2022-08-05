package nextstep.auth.authentication.filter.processing;

import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AuthenticationProcessingFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var authenticationToken = convert(request);
        var authentication = authenticate(authenticationToken);

        processing(authentication, response);

        return false;
    }

    protected abstract AuthenticationToken convert(HttpServletRequest request) throws IOException;

    protected abstract Authentication authenticate(AuthenticationToken authenticationToken);

    protected abstract void processing(Authentication authentication, HttpServletResponse response) throws Exception;

}
