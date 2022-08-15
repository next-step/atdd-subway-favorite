package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BearerTokenAuthenticationFilter implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

            jwtTokenProvider.validateToken(authCredentials);
            String principal = jwtTokenProvider.getPrincipal(authCredentials);
            List<String> roles = jwtTokenProvider.getRoles(authCredentials);

            Authentication authentication = new Authentication(principal, roles);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        } catch (RuntimeException exception) {
            return true;
        }
    }
}
