package nextstep.auth.authentication;

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
            //request로부터 token값을 추출한다(AuthorizationExtractor 활용)
            final String bearerToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

            //추출한 토큰에서 사용자 정보를 조회한다
            final AuthenticationToken token = new AuthenticationToken(bearerToken, bearerToken);

            //Authentication를 생성하고 SecurityContextHolder에 저장한다
            if (!jwtTokenProvider.validateToken(token.getCredentials())) {
                throw new AuthenticationException();
            }

            final Authentication authentication = new Authentication(jwtTokenProvider.getPrincipal(token.getPrincipal()), jwtTokenProvider.getRoles(token.getPrincipal()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return true;
        } catch (Exception e) {
            return true;
        }
    }

}
