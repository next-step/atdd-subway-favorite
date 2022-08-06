package nextstep.auth.member;

import java.util.List;

public interface UserDetails {
    String getEmail();

    String getPassword();

    List<String> getAuthorities();

    boolean checkPassword(String credentials);
}
