package nextstep.auth.unit.application;

import static nextstep.auth.support.GithubResponses.사용자;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.application.GithubTokenClient;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.exception.AuthenticationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.client.MockRestServiceServer;

@DisplayName("GithubClient 단위 테스트")
@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@RestClientTest(GithubTokenClient.class)
class GithubTokenClientTest {
  @Value("${github.url.access-token}")
  private String accessTokenUrl;

  @Autowired private GithubTokenClient githubTokenClient;
  @Autowired private MockRestServiceServer mockServer;
  @Autowired private ObjectMapper objectMapper;

  @DisplayName("Github API 를 통해서 액세스 토큰을 가져온다.")
  @Test
  void getAccessToken() throws JsonProcessingException {
    GithubAccessTokenResponse response =
        new GithubAccessTokenResponse(사용자.getAccessToken(), "repo,gist", "bearer");
    mockServer
        .expect(requestTo(accessTokenUrl))
        .andRespond(
            withSuccess(objectMapper.writeValueAsString(response), MediaType.APPLICATION_JSON));

    String accessToken = githubTokenClient.getAccessToken(사용자.getCode());

    assertThat(accessToken).isEqualTo(사용자.getAccessToken());
  }

  @DisplayName("Github API 에서 에러 메시지를 반환한다.")
  @Test
  void accessTokenErrors() {
    String errorResponse =
        "{\"error\":\"bad_verification_code\",\"error_description\":\"The code passed is incorrect"
            + " or expired.\",\"error_uri\":\"/apps/managing-oauth-apps/troubleshooting-oauth-app-access-token-request-errors/#bad-verification-code\"}";
    mockServer
        .expect(requestTo(accessTokenUrl))
        .andRespond(withSuccess(errorResponse, MediaType.APPLICATION_JSON));

    assertThatExceptionOfType(AuthenticationException.class)
        .isThrownBy(() -> githubTokenClient.getAccessToken("bad_verification_code"));
  }
}
