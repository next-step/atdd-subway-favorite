package nextstep.auth.user;

import java.util.List;

public interface UserDetail {
    boolean checkPassword(final String password);

    String getEmail();

    String getPassword();

    List<String> getAuthorities();
}
