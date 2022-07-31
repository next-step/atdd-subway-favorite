package nextstep.auth.service;

import java.util.List;

public interface UserDetails {

    String getEmail();

    List<String> getAuthorities();

    boolean checkPassword(String password);

}
