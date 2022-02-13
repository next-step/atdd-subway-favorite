package nextstep.auth.model.authentication.interceptor;

import nextstep.auth.model.authentication.AuthenticationToken;
import nextstep.auth.model.context.Authentication;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthenticationInterceptor extends HandlerInterceptor {
    @Override
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        AuthenticationToken authenticationToken = convert(request);
        Authentication authentication = authenticate(authenticationToken);
        afterAuthenticate(request, response, authentication);

        return false;
    }

    AuthenticationToken convert(HttpServletRequest request) throws IOException;

    Authentication authenticate(AuthenticationToken authenticationToken);

    void afterAuthenticate(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException;
}
