package nextstep.member.domain;

import nextstep.auth.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private final String email;
    private final String password;
    private final String role;

    public CustomUserDetails(String email, String password, String role) {
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
