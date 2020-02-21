package atdd.path.interceptor;

import atdd.path.application.JwtUtils;
import atdd.path.application.exception.InvalidTokenException;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AuthenticationInterceptor implements HandlerInterceptor {
    final String ACCESS_TOKEN_HEADER = "Authorization";
    final JwtUtils jwtUtils;

    public AuthenticationInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String accessToken = request.getHeader(ACCESS_TOKEN_HEADER);

        if (StringUtils.isEmpty(accessToken) || !jwtUtils.verify(accessToken))
            throw new InvalidTokenException();

        return true;
    }
}
