package nextstep.member.application;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {
    // 1. github token using code
    // 2. github profile using github token
    @Autowired
    private GithubClient githubClient;
    @Test
    void requestGithubToken() {
        String githubToken = githubClient.requestGithubToken("code");
        assertThat(githubToken).isNotBlank();
    }
}
