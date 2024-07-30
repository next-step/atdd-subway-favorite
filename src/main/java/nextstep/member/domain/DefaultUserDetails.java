package nextstep.member.domain;

import nextstep.auth.domain.UserDetails;

public class DefaultUserDetails implements UserDetails {
  private final String username;
  private final String password;

  public DefaultUserDetails(String username, String password) {
    this.username = username;
    this.password = password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public String getPassword() {
    return password;
  }
}
