package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContext;
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
        try {
            String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

            String userName = jwtTokenProvider.getPrincipal(token);
            List<String> roles = jwtTokenProvider.getRoles(token);

            Authentication authentication = new Authentication(userName, roles);
            SecurityContext securityContext = new SecurityContext(authentication);
            SecurityContextHolder.setContext(securityContext);
            return true;
        } catch (Exception e) {
            return true;
        }

    }
}
