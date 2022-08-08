package nextstep.auth.authentication.provider;

import lombok.RequiredArgsConstructor;
import nextstep.auth.User;
import nextstep.auth.UserDetailsService;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.context.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DefaultAuthenticationProvider implements AuthenticationProvider<AuthenticationToken>{

    private final UserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(AuthenticationToken token) {
        User user = userDetailsService.loadUserByUsername(token.getPrincipal());

        if (!user.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }

        return new Authentication(user.getUsername(), user.getAuthorities());
    }

}
