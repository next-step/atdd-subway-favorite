package nextstep.auth.user;

import java.util.List;

public interface UserDetails {
    String getEmail();

    String getPassword();

    List<String> getAuthorities();

    boolean checkPassword(String password);
}
