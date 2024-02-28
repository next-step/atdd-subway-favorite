package nextstep.member.application;

import nextstep.favorite.application.GithubClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {

    @DisplayName("github token 발급")
    @Test
    void requestGithubToken() {
        String code = "code";
        GithubClient githubClient = new GithubClient();

        String githubToken = githubClient.requestGithubToken(code);

        assertThat(githubToken).isNotBlank();
    }

}
