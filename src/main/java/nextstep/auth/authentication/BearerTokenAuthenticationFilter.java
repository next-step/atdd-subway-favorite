package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BearerTokenAuthenticationFilter implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

            if (!jwtTokenProvider.validateToken(authCredentials)) {
                throw new AuthenticationException();
            }

            Authentication authentication = new Authentication(jwtTokenProvider.getPrincipal(authCredentials),
                    jwtTokenProvider.getRoles(authCredentials));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
