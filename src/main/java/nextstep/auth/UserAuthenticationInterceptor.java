package nextstep.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.member.application.JwtTokenProvider;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserAuthenticationInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider tokenProvider;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZED_USER = "authorizedUser";
    private static final String TYPE_START = "Bearer ";

    public UserAuthenticationInterceptor(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (!StringUtils.hasLength(bearerToken) || !bearerToken.startsWith(TYPE_START)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        String token = bearerToken.substring(TYPE_START.length());

        if (!tokenProvider.validateToken(token)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        request.setAttribute(AUTHORIZED_USER, new AuthorizedUser(
            tokenProvider.getPrincipal(token), tokenProvider.getRoles(token)
        ));
        return true;
    }
}
