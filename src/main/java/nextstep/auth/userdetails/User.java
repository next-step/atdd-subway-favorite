package nextstep.auth.userdetails;

import java.util.List;

public class User implements UserDetails {

    private String email;
    private String password;
    private List<String> authorities;

    public User() {
    }

    public User(String email, String password, List<String> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public static User guest() {
        return new User();
    }

    public static User of(String email, List<String> authorities) {
        return new User(email, null, authorities);
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getPrincipal() {
        return email;
    }

    @Override
    public List<String> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isValidCredentials(String credentials) {
        return this.password.equals(credentials);
    }
}
