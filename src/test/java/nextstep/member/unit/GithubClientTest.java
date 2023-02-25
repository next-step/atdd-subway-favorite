package nextstep.member.unit;

import nextstep.member.application.GithubClient;
import nextstep.member.ui.GithubResponses;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = GithubClient.class)
public class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @Test
    void getAccessTokenFromGithub() {
        String accessToken = githubClient.getAccessTokenFromGithub(GithubResponses.사용자1.getCode());

        assertThat(accessToken).isNotBlank();
    }
}
