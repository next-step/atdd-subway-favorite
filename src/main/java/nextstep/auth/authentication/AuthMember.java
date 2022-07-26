package nextstep.auth.authentication;

import java.util.List;

public interface AuthMember {
    String getEmail();

    List<String> getAuthorities();

    boolean checkPassword(String password);
}
