package nextstep.auth.authentication.user;

import java.util.List;

public interface UserDetails {

  String getPrincipal();
  List<String> getAuthorities();
  boolean isValid(String credentials);
}
