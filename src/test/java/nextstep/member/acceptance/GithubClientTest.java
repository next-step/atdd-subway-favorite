package nextstep.member.acceptance;

import nextstep.member.application.GithubClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.member.acceptance.GithubResponse.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @DisplayName("깃헙 토큰 요청")
    @Test
    void requestGithubToken() {
        String accessCode = githubClient.requestGithubToken(사용자1.getCode());

        assertThat(accessCode).isEqualTo(사용자1.getAccessToken());
    }
}
