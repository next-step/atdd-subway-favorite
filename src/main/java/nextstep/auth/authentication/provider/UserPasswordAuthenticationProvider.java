package nextstep.auth.authentication.provider;

import lombok.RequiredArgsConstructor;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.UserDetails;
import nextstep.auth.authentication.UserDetailsService;
import nextstep.auth.context.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserPasswordAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;

    @Override
    public boolean supports(ProviderType providerType) {
        return providerType.isUserPasswordType();
    }

    @Override
    public Authentication authenticate(AuthenticationToken token) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(token.getPrincipal());
            if (!userDetails.checkPassword(token.getCredentials())) {
                throw new AuthenticationException();
            }
            return new Authentication(userDetails.getEmail(), userDetails.getAuthorities());
        } catch (RuntimeException e) {
            throw new AuthenticationException();
        }
    }
}
