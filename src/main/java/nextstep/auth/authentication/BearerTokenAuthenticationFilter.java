package nextstep.auth.authentication;

import java.util.Objects;

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
        try {
            String accessToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

            if (Objects.isNull(accessToken)) {
                throw new AuthenticationException();
            }

            if (!jwtTokenProvider.validateToken(accessToken)) {
                throw new AuthenticationException();
            }

            Authentication authentication = new Authentication(jwtTokenProvider.getPrincipal(accessToken),
                jwtTokenProvider.getRoles(accessToken));

            SecurityContextHolder.getContext().setAuthentication(authentication);

        }catch (Exception e){

        }
        return true;
    }
}
