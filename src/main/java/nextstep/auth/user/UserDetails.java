package nextstep.auth.user;

import java.util.List;

public interface UserDetails {
    boolean isEqualsPassword(String password);

    String getEmail();

    String getPassword();

    List<String> getAuthorities();
}
