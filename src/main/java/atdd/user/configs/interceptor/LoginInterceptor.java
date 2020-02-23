package atdd.user.configs.interceptor;

import atdd.user.application.JwtTokenProvider;
import atdd.user.application.TokenExtractor;
import atdd.user.application.exception.InvalidJwtAuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class LoginInterceptor extends HandlerInterceptorAdapter {
    JwtTokenProvider jwtTokenProvider;
    TokenExtractor tokenExtractor;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider){
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = tokenExtractor.extract(request);

        if (StringUtils.isEmpty(token) || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidJwtAuthenticationException("invalid token");
        }

        String email = jwtTokenProvider.getUserEmail(token);
        request.setAttribute("loginUserEmail", email);  // request에 로그인 유저의 email정보를 넣기
        return true;
    }
}
