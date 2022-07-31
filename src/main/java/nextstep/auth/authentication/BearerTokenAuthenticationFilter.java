package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class BearerTokenAuthenticationFilter implements HandlerInterceptor {
    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if(ObjectUtils.isEmpty(authCredentials)){
           return true;
        }

        if(!jwtTokenProvider.validateToken(authCredentials)){
            throw new AuthenticationException();
        }
        Authentication authentication = new Authentication(jwtTokenProvider.getPrincipal(authCredentials)
                , jwtTokenProvider.getRoles(authCredentials));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return true;
    }
}
