package nextstep.auth.authentication;

import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.LoginRequest;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class BearerTokenAuthenticationInterceptor extends ChainingAuthenticationInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    LoginRequest createLoginRequest(final HttpServletRequest request) {
        final String token = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        final String principal = jwtTokenProvider.getPrincipal(token);
        return new LoginRequest(principal, token);
    }

    @Override
    LoginMember findLoginMember(final LoginRequest request) {
        final List<String> roles = jwtTokenProvider.getRoles(request.getPassword());
        return new LoginMember(request.getEmail(), request.getPassword(), roles);
    }

    @Override
    boolean isAuthenticated(final LoginRequest loginRequest, final LoginMember loginMember) {
        return jwtTokenProvider.validateToken(loginMember.getPassword());
    }

}
