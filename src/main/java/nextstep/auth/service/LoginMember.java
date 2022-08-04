package nextstep.auth.service;

import java.util.List;

public class LoginMember implements UserDetails {
    private String email;
    private String password;
    private List<String> authorities;

    private LoginMember() {
    }

    private LoginMember(String email, String password, List<String> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static LoginMember guest() {
        return new LoginMember();
    }

    public static LoginMember of(String email, List<String> authorities) {
        return new LoginMember(email, null, authorities);
    }

    public static LoginMember of(String email, String password, List<String> authorities) {
        return new LoginMember(email, password, authorities);
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public List<String> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }
}
