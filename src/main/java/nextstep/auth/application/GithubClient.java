package nextstep.auth.application;

import nextstep.auth.GithubClientProperties;
import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class GithubClient {

  private final GithubClientProperties githubClientProperties;
  private final RestTemplate restTemplate;

  public GithubClient(GithubClientProperties githubClientProperties) {
    this.githubClientProperties = githubClientProperties;
    restTemplate = new RestTemplateBuilder()
        .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
        .rootUri(githubClientProperties.getBaseUrl())
        .messageConverters(new MappingJackson2HttpMessageConverter())
        .build();
  }

  public String requestGithubToken(final String code) {
    final var url = "/github/login/oauth/access_token";
    final var body = new GithubAccessTokenRequest(
        code,
        githubClientProperties.getId(),
        githubClientProperties.getSecret()
    );

    // TODO error handling
    return restTemplate
        .exchange(url, HttpMethod.POST, new HttpEntity<>(body), GithubAccessTokenResponse.class)
        .getBody()
        .getAccessToken();
  }

  public GithubProfileResponse requestGithubProfile(final String accessToken) {
    final var url = "/github/user";
    final var headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + accessToken);

    // TODO error handling
    return restTemplate
        .exchange(url, HttpMethod.POST, new HttpEntity<>(headers), GithubProfileResponse.class)
        .getBody();
  }
}
