package nextstep.auth.user;

import java.util.List;

/**
 * @author a1101466 on 2022/08/01
 * @project subway
 * @description
 */
public interface UserDetails {

    boolean isValidPassword(String password);

    String getEmail();

    String getPassword();

    List<String> getAuthorities();
}
