package nextstep.auth.authentication;

import java.util.List;

public interface UserDetails {

    String getUsername();

    String getPassword();

    List<String> getAuthorities();

}
