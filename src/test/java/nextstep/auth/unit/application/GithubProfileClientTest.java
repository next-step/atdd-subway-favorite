package nextstep.auth.unit.application;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import nextstep.auth.application.GithubProfileClient;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.exception.AuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(
    properties = {"github.url.profile=http://localhost:8090/github"},
    webEnvironment = SpringBootTest.WebEnvironment.NONE)
@WireMockTest(httpPort = 8090)
@DisplayName("GithubProfileClient 단위 테스트")
class GithubProfileClientTest {
  @Autowired private GithubProfileClient githubProfileClient;

  @DisplayName("Github 프로필을 조회한다.")
  @Test
  void getProfile() {
    stubFor(
        get(urlPathEqualTo("/github/user"))
            .withHeader(HttpHeaders.AUTHORIZATION, matching("Bearer .*"))
            .willReturn(
                aResponse()
                    .withStatus(HttpStatus.OK.value())
                    .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .withBody(
                        "{\"name\": \"monalisa octocat\", \"login\": \"octocat\", \"email\":"
                            + " \"octocat@github.com\"}")));

    GithubProfileResponse profile = githubProfileClient.getProfile("token");

    assertThat(profile.getName()).isEqualTo("monalisa octocat");
    assertThat(profile.getLogin()).isEqualTo("octocat");
    assertThat(profile.getEmail()).isEqualTo("octocat@github.com");
  }

  @DisplayName("Github API 에러 응답을 예외 처리한다.")
  @Test
  void handleError() {
    stubFor(
        get(urlPathEqualTo("/github/user"))
            .withHeader(HttpHeaders.AUTHORIZATION, matching("Bearer .*"))
            .willReturn(aResponse().withStatus(HttpStatus.UNAUTHORIZED.value())));

    assertThatExceptionOfType(AuthenticationException.class)
        .isThrownBy(() -> githubProfileClient.getProfile("token"));
  }
}
