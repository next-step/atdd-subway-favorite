package atdd.path.application.interceptor;

import atdd.path.application.exception.InvalidJwtAuthenticationException;
import atdd.path.application.provider.JwtTokenProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static atdd.path.application.provider.JwtTokenProvider.TOKEN_TYPE;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public LoginInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = extractToken(request);

        if (StringUtils.isEmpty(token) || !jwtTokenProvider.validateToken(token)) {
            throw new InvalidJwtAuthenticationException("invalid token");
        }

        String email = jwtTokenProvider.getUserEmail(token);
        request.setAttribute("loginUserEmail", email);  // request에 로그인 유저의 email정보를 넣기
        return true;
    }

    private String extractToken(HttpServletRequest request) {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.isEmpty(header)) {
            return "";
        }

        final String[] parts = header.split(" ");

        if (parts.length == 2) {
            return TOKEN_TYPE.equalsIgnoreCase(parts[0]) ? parts[1] : "";
        }

        return "";
    }

}
