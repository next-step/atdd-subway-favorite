package atdd.member.interceptor;

import atdd.member.exception.InvalidAuthenticationTokenException;
import atdd.member.security.JwtAuthenticationProvider;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    public static final String AUTHORIZATION = "Authorization";
    public static final String TOKEN_TYPE = "Bearer ";

    private final JwtAuthenticationProvider provider;

    public LoginInterceptor(JwtAuthenticationProvider provider) {
        this.provider = provider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(AUTHORIZATION);

        if (!StringUtils.startsWithIgnoreCase(token, TOKEN_TYPE) || provider
            .isExpired(token = token.replace(TOKEN_TYPE, ""))) {
            throw new InvalidAuthenticationTokenException("Expired. or Invalid Token");
        }
        request.setAttribute("loginEmail", provider.getEmailFromToken(token));
        return true;
    }
}
