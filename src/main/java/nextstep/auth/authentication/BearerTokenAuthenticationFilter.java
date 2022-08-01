package nextstep.auth.authentication;

import java.util.List;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
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
        // TODO: 구현하세요.
        try {
            String auth = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
            AuthenticationToken token = new AuthenticationToken(auth, auth);

            if (!jwtTokenProvider.validateToken(token.getCredentials())) {
                throw new AuthenticationException();
            }

            String principal = jwtTokenProvider.getPrincipal(token.getPrincipal());
            List<String> roles = jwtTokenProvider.getRoles(token.getPrincipal());

            Authentication authentication = new Authentication(principal, roles);

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
