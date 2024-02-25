package nextstep.member.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubAccessTokenRequest {

  String code;
  String clientId;
  String clientSecret;
}
