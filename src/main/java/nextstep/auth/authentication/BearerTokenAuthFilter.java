package nextstep.auth.authentication;

import lombok.RequiredArgsConstructor;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.interceptor.AuthChainInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
public class BearerTokenAuthFilter extends AuthChainInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void execute(final HttpServletRequest request, final HttpServletResponse response) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        AuthenticationToken token = new AuthenticationToken(authCredentials, authCredentials);

        if (!jwtTokenProvider.validateToken(token.getPrincipal())) {
            throw new AuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(token.getPrincipal());
        List<String> roles = jwtTokenProvider.getRoles(token.getPrincipal());

        Authentication authentication = new Authentication(principal, roles);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
