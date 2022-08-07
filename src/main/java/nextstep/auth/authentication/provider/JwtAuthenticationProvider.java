package nextstep.auth.authentication.provider;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider<AuthenticationToken>{
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Authentication authenticate(AuthenticationToken authenticate) {
        if(!jwtTokenProvider.validateToken(authenticate.getCredentials())) {
            throw new AuthenticationException();
        }

        String userName = jwtTokenProvider.getPrincipal(authenticate.getCredentials());
        List<String> roles = jwtTokenProvider.getRoles(authenticate.getCredentials());

        return new Authentication(userName, roles);
    }
}
