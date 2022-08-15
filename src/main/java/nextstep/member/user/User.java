package nextstep.member.user;

import java.util.List;

public interface User {
    boolean checkPassword(String password);

    String getPrincipal();

    List<String> getAuthorities();
}
