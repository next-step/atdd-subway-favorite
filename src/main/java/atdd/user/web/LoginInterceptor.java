package atdd.user.web;

import atdd.path.application.JwtTokenProvider;
import atdd.user.application.exception.FailedLoginException;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private static final String LOGIN_USER_EMAIL = "loginUserEmail";

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (!jwtTokenProvider.validateToken(token)) {
            throw new FailedLoginException.InvalidJwtAuthenticationException("Invalid token");
        }

        String email = jwtTokenProvider.getUserEmail(token);
        request.setAttribute(LOGIN_USER_EMAIL, email);
        return true;
    }
}
