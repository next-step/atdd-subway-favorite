package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BearerTokenAuthenticationFilter implements HandlerInterceptor {
    private final static String BEARER_TOKEN_PREFIX = "Bearer ";
    private final static String HEADER_KEY = "Authorization";
    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        setAuthentication(request);
        return true;
    }

    private void setAuthentication(HttpServletRequest request) {
        final String authorization = request.getHeader(HEADER_KEY);

        if (authorization != null && authorization.startsWith(BEARER_TOKEN_PREFIX)) {
            final String bearerToken = authorization.substring(BEARER_TOKEN_PREFIX.length());
            validation(bearerToken);
            final Authentication authentication = new Authentication(
                    jwtTokenProvider.getPrincipal(bearerToken),
                    jwtTokenProvider.getRoles(bearerToken)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private void validation(String bearerToken) {
        if (!jwtTokenProvider.validateToken(bearerToken)) {
            throw new AuthenticationException();
        }
    }
}
