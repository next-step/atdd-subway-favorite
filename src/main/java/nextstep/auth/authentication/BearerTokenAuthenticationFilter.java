package nextstep.auth.authentication;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.web.servlet.HandlerInterceptor;

public class BearerTokenAuthenticationFilter implements HandlerInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (checkAlreadyAuthentication()) {
            return true;
        }

        String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        if (token.isBlank()) {
            return true;
        }

        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(token);
        List<String> credentials = jwtTokenProvider.getRoles(token);
        Authentication authentication = new Authentication(principal, credentials);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }

    private boolean checkAlreadyAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null;
    }
}
