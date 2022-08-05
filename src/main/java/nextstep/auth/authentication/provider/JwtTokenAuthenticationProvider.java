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
public class JwtTokenAuthenticationProvider implements AuthenticationProvider {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supports(ProviderType providerType) {
        return providerType.isJwtTokenType();
    }

    @Override
    public Authentication authenticate(AuthenticationToken token) {
        try {
            if (!jwtTokenProvider.validateToken(token.getPrincipal())) {
                throw new AuthenticationException();
            }

            String principal = jwtTokenProvider.getPrincipal(token.getPrincipal());
            List<String> roles = jwtTokenProvider.getRoles(token.getPrincipal());
            return new Authentication(principal, roles);
        } catch (Exception e) {
            throw new AuthenticationException();
        }
    }
}
