package nextstep.auth.interceptors;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
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
        try {
            String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

            boolean isValidToken = jwtTokenProvider.validateToken(token);
            if (!isValidToken) {
                throw new AuthenticationException();
            }

            String principal = jwtTokenProvider.getPrincipal(token);
            List<String> roles = jwtTokenProvider.getRoles(token);

            Authentication authentication = new Authentication(principal, roles);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        } catch (AuthenticationException e) {
            return true;
        }
    }
}
