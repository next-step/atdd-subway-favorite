package nextstep.auth.authentication.filter.chain;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

public class BearerTokenAuthenticationFilter extends ChainInterceptor {
    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected AuthenticationToken getAuthenticationToken(HttpServletRequest request) {
        String accessToken = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        return new AuthenticationToken(null, accessToken);
    }

    @Override
    protected Authentication getAuthentication(AuthenticationToken authenticationToken) {
        String credentials = authenticationToken.getCredentials();
        if (Objects.isNull(credentials)) {
            throw new AuthenticationException();
        }

        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new AuthenticationException();
        }

        return new Authentication(jwtTokenProvider.getPrincipal(credentials),
                jwtTokenProvider.getRoles(credentials));

    }

    @Override
    protected void doAuthentication(Authentication authentication, HttpServletResponse response) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
