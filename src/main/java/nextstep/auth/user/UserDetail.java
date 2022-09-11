package nextstep.auth.user;

import java.util.List;

public interface UserDetail {
    String getEmail();

    String getPassword();

    List<String> getAuthorities();

    boolean checkPassword(String password);
}
