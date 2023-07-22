package nextstep.api.member.application.auth;

import nextstep.api.auth.application.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
    private final String email;
    private final String password;
    private final String role;

    public CustomUserDetails(final String email, final String password, final String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getRole() {
        return role;
    }
}
