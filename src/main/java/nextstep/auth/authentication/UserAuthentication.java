package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserAuthentication {

    private final UserDetailsService userDetailsService;

    public UserAuthentication(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public Authentication authenticate(AuthenticationToken token) {
        String principal = token.getPrincipal();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(principal);
        checkAuthentication(userDetails, token);
        return new Authentication(userDetails);
    }

    private void checkAuthentication(UserDetails userDetails, AuthenticationToken token) {
        if (userDetails == null) {
            throw new AuthenticationException();
        }

        if (!userDetails.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }
    }
}
