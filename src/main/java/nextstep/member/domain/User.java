package nextstep.member.domain;

import java.util.List;
import nextstep.auth.authentication.user.UserDetails;

public class User implements UserDetails {

  private String email;
  private String password;
  private List<String> authorities;

  public User() {}

  public User(String email, String password, List<String> authorities) {
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  public static User guest() {
    return new User();
  }

  public static User of(String email, List<String> authorities) {
    return new User(email, null, authorities);
  }

  public String getEmail() {
    return email;
  }

  @Override
  public String getPrincipal() {
    return this.email;
  }

  @Override
  public List<String> getAuthorities() {
    return this.authorities;
  }

  @Override
  public boolean isValid(String credentials) {
    return this.password.equals(credentials);
  }
}
