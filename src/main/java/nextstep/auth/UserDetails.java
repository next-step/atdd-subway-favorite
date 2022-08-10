package nextstep.auth;

import java.util.List;

public interface UserDetails {
    Long getUserId();
    List<String> getAuthorities();
    boolean checkPassword(String password);
}
