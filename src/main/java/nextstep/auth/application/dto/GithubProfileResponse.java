package nextstep.auth.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class GithubProfileResponse {
  private final String login;
  private final String name;
  private final String email;

  public GithubProfileResponse(String login, String name, String email) {
    this.login = login;
    this.name = name;
    this.email = email;
  }
}
