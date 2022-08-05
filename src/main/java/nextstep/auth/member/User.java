package nextstep.auth.member;

import java.util.List;

public class User implements UserDetails {
    private String email;
    private String password;
    private List<String> authorities;


    public User(String email, String password, List<String> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    public User(String email, String password) {
        this(email, password, null);
    }

    public static User of(String email, String password, List<String> authorities) {
        return new User(email, password, authorities);
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
    public List<String> getAuthorities() {
        return authorities;
    }
}
