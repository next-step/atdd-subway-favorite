package nextstep.member.application;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GithubClientTest {

  @Autowired
  GithubClient githubClient;

  @DisplayName("인증 서버를 호출하고 인증서버로부터 획득한 토큰을 반환한다.")
  @Test
  void requestGithubToken() {
    // given
    var code = "domodazzi";

    // when
    var accessToken = githubClient.requestGithubToken(code);

    // then
    assertThat(accessToken).isEqualTo(code + " access token");
  }
}