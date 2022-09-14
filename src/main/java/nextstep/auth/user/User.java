package nextstep.auth.user;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User {
    private String email;
    private String password;
    private List<String> authorities;

    public static User of(String email, List<String> authorities) {
        return new User(email, null, authorities);
    }

    public static User of(String email, String password, List<String> authorities) {
        return new User(email, password, authorities);
    }

    public static User guest() {
        return new User();
    }

    public boolean checkPassword(final String password) {
        return this.password.equals(password);
    }
}
