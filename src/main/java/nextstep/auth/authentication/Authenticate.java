package nextstep.auth.authentication;

import nextstep.auth.user.User;
import nextstep.auth.user.UserDetailsService;

public class Authenticate {

    private final UserDetailsService userDetailsService;

    public Authenticate(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public User execute(String principal, String credentials) {
        User user = userDetailsService.loadUserByUsername(principal);

        if (user == null) {
            throw new AuthenticationException();
        }

        if (!user.checkPassword(credentials)) {
            throw new AuthenticationException();
        }

        return user;
    }
}
