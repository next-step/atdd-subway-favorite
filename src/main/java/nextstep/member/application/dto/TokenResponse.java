package nextstep.member.application.dto;

import lombok.Getter;

@Getter
public class TokenResponse {
  private final String accessToken;

  public TokenResponse() {
    this(null);
  }

  public TokenResponse(String accessToken) {
    this.accessToken = accessToken;
  }
}
