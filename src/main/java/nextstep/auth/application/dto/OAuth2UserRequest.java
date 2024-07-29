package nextstep.auth.application.dto;

import lombok.Getter;

@Getter
public class OAuth2UserRequest {
  private final String accessToken;

  public OAuth2UserRequest(String accessToken) {
    this.accessToken = accessToken;
  }
}
