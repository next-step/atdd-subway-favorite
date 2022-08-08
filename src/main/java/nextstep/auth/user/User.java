package nextstep.auth.user;


import java.util.List;

public class User implements UserDetails {
    private final String email;
    private final String password;
    private final List<String> authorities;

    public static UserDetails of(String email, List<String> authorities) {
        return new User(email, null, authorities);
    }


    public User(String email, String password, List<String> authorities) {
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    @Override
    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String getPrincipal() {
        return email;
    }

    @Override
    public List<String> getAuthorities() {
        return authorities;
    }
}
