package nextstep.auth.userdetails;

import java.util.List;

public interface UserDetails {

    String getPrincipal();

    String getCredential();

    List<String> getAuthorities();

    boolean checkPassword(String password);
}
