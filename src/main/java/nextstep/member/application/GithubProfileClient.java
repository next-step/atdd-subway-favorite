package nextstep.member.application;

import lombok.RequiredArgsConstructor;
import nextstep.member.application.dto.GithubProfileResponse;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GithubProfileClient {
  private final GithubProfileApi githubProfileApi;

  public GithubProfileResponse getProfile(String accessToken) {
    return githubProfileApi.getProfile("Bearer " + accessToken);
  }
}
