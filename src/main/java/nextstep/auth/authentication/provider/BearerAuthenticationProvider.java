package nextstep.auth.authentication.provider;

import static nextstep.auth.authentication.execption.InvalidTokenException.INVALID_TOKEN;

import java.util.List;
import nextstep.auth.authentication.AuthenticationManager;
import nextstep.auth.authentication.execption.AuthenticationException;
import nextstep.auth.authentication.execption.InvalidTokenException;
import nextstep.auth.authentication.token.BearerAuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;

public class BearerAuthenticationProvider implements AuthenticationManager {
    private final JwtTokenProvider jwtTokenProvider;

    public BearerAuthenticationProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String credentials = (String) authentication.getCredentials();

        if (credentials.isBlank()) {
            throw new AuthenticationException();
        }

        if (!jwtTokenProvider.validateToken(credentials)) {
            throw new InvalidTokenException(INVALID_TOKEN);
        }

        String principal = jwtTokenProvider.getPrincipal(credentials);
        List<String> roles = jwtTokenProvider.getRoles(credentials);

        return new BearerAuthenticationToken(principal, roles);
    }
}