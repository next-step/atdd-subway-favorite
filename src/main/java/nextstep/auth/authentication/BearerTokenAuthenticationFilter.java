package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BearerTokenAuthenticationFilter extends KeepProceedAuthenticationFilter {

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
            List<String> roles = jwtTokenProvider.getRoles(token);

            Authentication authentication = new Authentication(principal, roles);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return proceed();
        } catch (Exception e) {
            return proceed();
        }
    }
}
