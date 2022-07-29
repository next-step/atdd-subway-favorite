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
        if(isAlreadyLoginUser()) {
            return true;
        }

        try {
            String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
            registerAuthentication(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean isAlreadyLoginUser() {
        return SecurityContextHolder.getContext().getAuthentication() != null;
    }

    private void registerAuthentication(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new AuthenticationException();
        }

        String email = jwtTokenProvider.getPrincipal(token);
        List<String> roles = jwtTokenProvider.getRoles(token);
        SecurityContextHolder.getContext().setAuthentication(new Authentication(email, roles));
    }
}
