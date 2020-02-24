package atdd.user.configs.interceptor;

import atdd.user.application.JwtTokenProvider;
import atdd.user.application.exception.InvalidJwtAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    private static final String HEADER_AUTH = "Authorization";
    private JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = jwtTokenProvider.resolveToken(request);

        if (StringUtils.isEmpty(token) || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidJwtAuthenticationException("invalid token");
        }
        String email = jwtTokenProvider.getUserEmail(token);
        request.setAttribute("loginUserEmail", email);
        return true;
    }
}
