package nextstep.subway.acceptance.auth;

import nextstep.auth.domain.UserDetails;

public class UserDetailsImpl implements UserDetails {
    private final Long id;
    private final String email;
    private final String password;

    public UserDetailsImpl(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
