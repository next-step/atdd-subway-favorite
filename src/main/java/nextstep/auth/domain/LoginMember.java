package nextstep.auth.domain;

import lombok.Getter;

@Getter
public class LoginMember {
  private final String email;

  public LoginMember(String email) {
    this.email = email;
  }
}
