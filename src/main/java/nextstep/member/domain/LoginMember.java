package nextstep.member.domain;

import lombok.Getter;

@Getter
public class LoginMember {
  private final String email;

  public LoginMember(String email) {
    this.email = email;
  }
}
