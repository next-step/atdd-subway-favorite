package nextstep.auth.userdetail;

import java.util.List;

public interface UserDetails {

    String getPrincipal();

    List<String> getAuthorities();

    boolean isInvalidCredentials(String credentials);

}
