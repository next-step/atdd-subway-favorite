package atdd.path.security;

import atdd.path.application.BearerTokenExtractor;
import atdd.path.application.JwtTokenProvider;
import atdd.path.application.exception.InvalidJwtAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    private static final Logger log = LoggerFactory.getLogger(LoginInterceptor.class);

    private final JwtTokenProvider jwtTokenProvider;
    private final BearerTokenExtractor tokenExtractor;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider, BearerTokenExtractor tokenExtractor) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.tokenExtractor = tokenExtractor;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = tokenExtractor.extract(request);

        if (StringUtils.isEmpty(token) || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidJwtAuthenticationException("invalid token");
        }

        String email = jwtTokenProvider.getUserEmail(token);
        request.setAttribute("loginUserEmail", email);
        return true;
    }
}
