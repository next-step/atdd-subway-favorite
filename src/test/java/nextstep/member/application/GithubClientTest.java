package nextstep.member.application;

import nextstep.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {

    @DisplayName("code로 깃헙 토큰을 요청 한다.")
    @Test
    void requestGithubToken() {
        final String code = GithubResponses.사용자1.code();
        GithubClient githubClient = new GithubClient();

        String githubToken = githubClient.requestGithubToken(code);

        assertThat(githubToken).isNotBlank();
        assertThat(githubToken).isEqualTo(GithubResponses.사용자1.accessToken());
    }

    @DisplayName("깃헙토큰으로 Profile을 요청 한다.")
    @Test
    void requestGithubProfile() {

    }
}

