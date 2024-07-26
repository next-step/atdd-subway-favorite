package nextstep.member.application.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GithubAccessTokenRequest {
  private final String clientId;
  private final String clientSecret;
  private final String code;

  private GithubAccessTokenRequest(String clientId, String clientSecret, String code) {
    this.clientId = clientId;
    this.clientSecret = clientSecret;
    this.code = code;
  }

  public static GithubAccessTokenRequest of(String clientId, String clientSecret, String code) {
    return new GithubAccessTokenRequest(clientId, clientSecret, code);
  }
}
