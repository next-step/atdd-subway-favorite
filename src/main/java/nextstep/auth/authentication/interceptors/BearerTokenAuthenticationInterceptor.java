package nextstep.auth.authentication.interceptors;

import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.authentication.AuthenticateRequest;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthenticationInterceptor extends ChainingAuthenticationInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    AuthenticateRequest createLoginRequest(final HttpServletRequest request) {
        final String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        final String principal = jwtTokenProvider.getPrincipal(token);
        return new AuthenticateRequest(principal, token);
    }

    @Override
    LoginMember findLoginMember(final AuthenticateRequest request) {
        final List<String> roles = jwtTokenProvider.getRoles(request.getPassword());
        return new LoginMember(request.getEmail(), request.getPassword(), roles);
    }

    @Override
    boolean isAuthenticated(final AuthenticateRequest authenticateRequest, final LoginMember loginMember) {
        return jwtTokenProvider.validateToken(loginMember.getPassword());
    }

}
