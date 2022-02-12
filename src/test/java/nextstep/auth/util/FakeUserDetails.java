package nextstep.auth.util;

import nextstep.auth.UserDetails;

public class FakeUserDetails implements UserDetails {

    private final String password;

    public FakeUserDetails(final String password) {
        this.password = password;
    }

    @Override
    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }
}
