package nextstep.auth.filters.provider;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.user.UserDetails;
import nextstep.auth.user.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class DefaultAuthenticationProvider implements AuthenticationProvider<AuthenticationToken> {
    private final UserDetailsService userDetailsService;

    public DefaultAuthenticationProvider(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public UserDetails provide(AuthenticationToken token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getPrincipal());
        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
        return userDetails;
    }
}
