package nextstep.auth.filters.provider;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.user.User;
import nextstep.auth.user.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider<String> {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationProvider(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public UserDetails provide(String token) {
        boolean isValidToken = jwtTokenProvider.validateToken(token);
        if (!isValidToken) {
            throw new AuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(token);
        List<String> roles = jwtTokenProvider.getRoles(token);
        return User.of(principal, roles);
    }
}
