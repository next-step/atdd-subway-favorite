package nextstep.auth;

import java.util.List;

public interface UserDetails {
    boolean checkPassword(String password);

    String getUsername();

    String getPassword();

    List<String> getAuthorities();
}
