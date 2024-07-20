package nextstep.member.application.dto;

import lombok.Getter;

@Getter
public class TokenRequest {
  private final String email;
  private final String password;

  public TokenRequest() {
    this(null, null);
  }

  public TokenRequest(String email, String password) {
    this.email = email;
    this.password = password;
  }
}
