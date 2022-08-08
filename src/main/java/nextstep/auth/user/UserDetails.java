package nextstep.auth.user;

import java.util.List;

public interface UserDetails {
    boolean checkPassword(String password);

    String getPrincipal();

    List<String> getAuthorities();
}
