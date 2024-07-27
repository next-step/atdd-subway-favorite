package nextstep.auth.application;

import nextstep.auth.application.dto.GithubAccessTokenRequest;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.exception.AuthenticationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GithubTokenClient {
  @Value("${github.url.access-token}")
  private String accessTokenUrl;

  @Value("${github.client.id}")
  private String clientId;

  @Value("${github.client.secret}")
  private String clientSecret;

  private final RestTemplate restTemplate;

  public GithubTokenClient(RestTemplateBuilder restTemplateBuilder) {
    this.restTemplate = restTemplateBuilder.build();
  }

  public String getAccessToken(String code) {
    GithubAccessTokenRequest request = GithubAccessTokenRequest.of(clientId, clientSecret, code);

    HttpHeaders headers = new HttpHeaders();
    headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

    HttpEntity<GithubAccessTokenRequest> httpEntity = new HttpEntity<>(request, headers);
    GithubAccessTokenResponse response =
        restTemplate
            .exchange(accessTokenUrl, HttpMethod.POST, httpEntity, GithubAccessTokenResponse.class)
            .getBody();
    if (response == null) {
      throw new AuthenticationException();
    }

    String accessToken = response.getAccessToken();
    if (accessToken == null) {
      throw new AuthenticationException();
    }
    return accessToken;
  }
}
