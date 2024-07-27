package nextstep.auth.application.dto;

import lombok.Getter;

@Getter
public class GithubLoginRequest {
  private final String code;

  public GithubLoginRequest(String code) {
    this.code = code;
  }
}
