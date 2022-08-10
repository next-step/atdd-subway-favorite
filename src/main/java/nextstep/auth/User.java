package nextstep.auth;

import java.util.List;

public class User implements UserDetails {
    private Long userId;
    private String password;
    private List<String> authorities;

    public static User of(Long userId, String password, List<String> roles) {
        return new User(userId, password, roles);
    }

    public static User of(Long userId, List<String> authorities) {
        return new User(userId, null, authorities);
    }

    public static User guest() {
        return new User();
    }

    public User() {
    }

    public User(Long userId, String password, List<String> authorities) {
        this.userId = userId;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public Long getUserId() {
        return userId;
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
