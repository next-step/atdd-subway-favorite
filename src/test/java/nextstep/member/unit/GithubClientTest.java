package nextstep.member.unit;

import nextstep.member.application.oauth.GithubClient;
import nextstep.member.ui.dto.GithubLoginRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {
    @Autowired
    GithubClient githubClient;

    @Test
    @DisplayName("토큰 요청 정상 동작")
    void succeedToGetToken() {
        GithubLoginRequest request = new GithubLoginRequest("code");

        String token = githubClient.requestToken(request);
        assertThat(token).isEqualTo("access_token");
    }
}
