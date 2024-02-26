package nextstep.member.application;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GithubClientTest {
    // 1. github token using code
    // 2. github profile using github token

    @Test
    void requestGithubToken() {
        GithubClient githubClient = new GithubClient();

        String githubToken = githubClient.requestGithubToken("code");
        assertThat(githubToken).isNotBlank();
    }
}
