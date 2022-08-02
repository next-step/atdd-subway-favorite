package nextstep.auth.interceptor.chain;

import javax.servlet.http.HttpServletRequest;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;

public class BearerTokenAuthenticationFilter extends AuthChainInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected Authentication createAuthentication(final HttpServletRequest request) {
        //request로부터 token값을 추출한다(AuthorizationExtractor 활용)
        final String bearerToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        //추출한 토큰에서 사용자 정보를 조회한다
        final AuthenticationToken token = new AuthenticationToken(bearerToken, bearerToken);

        //Authentication를 생성하고 SecurityContextHolder에 저장한다
        if (!jwtTokenProvider.validateToken(token.getCredentials())) {
            throw new AuthenticationException();
        }

        return new Authentication(jwtTokenProvider.getPrincipal(token.getPrincipal()), jwtTokenProvider.getRoles(token.getPrincipal()));
    }

}
