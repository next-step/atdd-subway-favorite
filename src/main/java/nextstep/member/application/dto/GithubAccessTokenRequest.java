package nextstep.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubAccessTokenRequest {

  private String code;
  private String clientId;
  private String clientSecret;
}
