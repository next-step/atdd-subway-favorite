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
        ValidateUser validateUser = new ValidateUser();
        validateUser.execute(credentials, user);
        return user;
    }
}
