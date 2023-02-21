package nextstep.common.interceptor;

import nextstep.common.exception.LoginException;
import nextstep.common.utils.JwtTokenProvider;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;

@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final String AUTHORIZATION = "Authorization";
    private static final String BEARER = "Bearer";
    private static final String PRINCIPAL_KEY = "principal";
    private static final String ACCESS_TOKEN_KEY = "accessToken";

    private final JwtTokenProvider jwtTokenProvider;

    public JwtInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = extractAuthScheme(request, BEARER);
        if (Strings.isBlank(token)) {
            return true;
        }
        if (!jwtTokenProvider.validateToken(token)) {
            throw new LoginException("유효하지 않은 토큰입니다.");
        }
        String principal = jwtTokenProvider.getPrincipal(token);
        request.setAttribute(PRINCIPAL_KEY, principal);
        request.setAttribute(ACCESS_TOKEN_KEY, token);
        return true;
    }


    public String extractAuthScheme(HttpServletRequest request, String type) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);
        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (value.toLowerCase().startsWith(type.toLowerCase())) {
                return value.substring(type.length()).trim();
            }
        }
        return Strings.EMPTY;
    }
}
