package nextstep.auth.user;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    private String email;
    private String password;
    private List<String> authorities;

    public static User of(String email, List<String> authorities) {
        return new User(email, null, authorities);
    }

    public static User guest() {
        return new User();
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public boolean isEqualsPassword(String password) {
        return !checkPassword(password);
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
