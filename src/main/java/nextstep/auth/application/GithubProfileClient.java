package nextstep.auth.application;

import lombok.RequiredArgsConstructor;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubProfileClient {
  private final GithubProfileApi githubProfileApi;

  public GithubProfileResponse getProfile(String accessToken) {
    return githubProfileApi.getProfile("Bearer " + accessToken);
  }
}
