package nextstep.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubAccessTokenResponse {
  private final String accessToken;
  private final String scope;
  private final String tokenType;

  public GithubAccessTokenResponse(String accessToken, String scope, String tokenType) {
    this.accessToken = accessToken;
    this.scope = scope;
    this.tokenType = tokenType;
  }
}
