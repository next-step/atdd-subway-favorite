package nextstep.auth.authentication;

import java.util.List;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BearerTokenAuthenticationFilter implements HandlerInterceptor {

    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

            if (!jwtTokenProvider.validateToken(token)) {
                throw new AuthenticationException();
            }

            String principal = jwtTokenProvider.getPrincipal(token);
            List<String> authorities = jwtTokenProvider.getRoles(token);

            Authentication authentication = new Authentication(principal, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
