package nextstep.auth.application;

import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.acceptance.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {

    @Autowired
    GithubClient githubClient;

    @DisplayName("github token 발급")
    @Test
    void requestGithubToken() {
        String githubToken = githubClient.requestGithubToken(GithubResponses.사용자1.getCode());

        assertThat(githubToken).isEqualTo(GithubResponses.사용자1.getAccessToken());
    }

    @DisplayName("github profile 조회")
    @Test
    void requestGithubProfile() {
        GithubProfileResponse response = githubClient.requestGithubProfile(GithubResponses.사용자2.getAccessToken());

        assertSoftly(softly -> {
            softly.assertThat(response.getEmail()).isEqualTo(GithubResponses.사용자2.getEmail());
            softly.assertThat(response.getAge()).isEqualTo(GithubResponses.사용자2.getAge());
        });
    }
}

