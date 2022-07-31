package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.exception.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;

public class BearerTokenAuthenticationFilter extends AuthenticationChainHandler {
    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected Authentication createAuthentication(HttpServletRequest request) {

        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);

        if(ObjectUtils.isEmpty(authCredentials)){
            throw new AuthenticationException();
        }

        if(!jwtTokenProvider.validateToken(authCredentials)){
            throw new AuthenticationException();
        }

        return new Authentication(jwtTokenProvider.getPrincipal(authCredentials)
                , jwtTokenProvider.getRoles(authCredentials));
    }
}
