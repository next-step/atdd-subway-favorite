package nextstep.member.domain;


import java.util.List;

public interface User {
    String getEmail();
    List<String> getAuthorities();

    boolean checkPassword(String password);
}
