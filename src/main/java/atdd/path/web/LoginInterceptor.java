package atdd.path.web;

import atdd.path.jwt.JwtTokenProvider;
import atdd.path.jwt.ReadProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private JwtTokenProvider jwtTokenProvider;
    private ReadProperties readProperties;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider, ReadProperties readProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.readProperties = readProperties;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = jwtTokenProvider.resolveToken(request);
        String secretKey = readProperties.getSecretKey();
        boolean isValidToken = jwtTokenProvider.validateToken(token);
        String email = jwtTokenProvider.getUserEmail(token);
        request.setAttribute("email", email);
        return true;
    }
}
