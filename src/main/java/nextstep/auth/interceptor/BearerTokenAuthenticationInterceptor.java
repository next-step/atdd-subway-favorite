package nextstep.auth.interceptor;

import nextstep.auth.authentication.AuthMemberLoader;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.converter.AuthenticationConverter;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static nextstep.auth.authentication.AuthorizationExtractor.extract;

public class BearerTokenAuthenticationInterceptor extends AuthenticationInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationInterceptor(AuthenticationConverter authenticationConverter, AuthMemberLoader authMemberLoader, JwtTokenProvider jwtTokenProvider) {
        super(authenticationConverter, authMemberLoader);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String bearerToken = extract(request, AuthorizationType.BEARER);
        if (bearerToken.isEmpty()) {
            return true;
        }

        validation(bearerToken);
        String principal = jwtTokenProvider.getPrincipal(bearerToken);
        List<String> roles = jwtTokenProvider.getRoles(bearerToken);
        Authentication authentication = new Authentication(principal, roles);
        afterAuthentication(request, response, authentication);
        return true;
    }

    @Override
    public void afterAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void validation(String bearerToken) {
        if (!jwtTokenProvider.validateToken(bearerToken)) {
            throw new AuthenticationException();
        }
    }
}
