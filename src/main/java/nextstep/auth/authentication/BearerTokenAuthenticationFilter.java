package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BearerTokenAuthenticationFilter implements HandlerInterceptor {
    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        final String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if (!jwtTokenProvider.validateToken(token)) {
            return true;
        }

        Authentication authentication = createAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }

    private Authentication createAuthentication(final String token) {
        final String principal = jwtTokenProvider.getPrincipal(token);
        final List<String> credentials = jwtTokenProvider.getRoles(token);

        return new Authentication(principal, credentials);
    }
}
