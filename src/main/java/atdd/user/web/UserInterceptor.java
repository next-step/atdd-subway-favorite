package atdd.user.web;

import atdd.path.application.JwtTokenProvider;
import atdd.user.application.exception.FailedLoginException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UserInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String LOGIN_USER_EMAIL = "loginUserEmail";
    private static final String HEADER_AUTHORIZATION = "Authorization";

    public UserInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HEADER_AUTHORIZATION);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new FailedLoginException.InvalidJwtAuthenticationException("Invalid token");
        }

        String email = jwtTokenProvider.getUserEmail(token);
        request.setAttribute(LOGIN_USER_EMAIL, email);
        return true;
    }
}
