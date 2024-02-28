package nextstep.member.application;

import nextstep.favorite.application.GithubClient;
import nextstep.favorite.application.dto.GithubProfileResponse;
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
        String code = "code";
        String githubToken = githubClient.requestGithubToken(code);
        assertThat(githubToken).isNotBlank();
    }

    @DisplayName("github profile 조회")
    @Test
    void requestGithubProfile() {
        String code = "code";
        String accessToken = githubClient.requestGithubToken(code);

        GithubProfileResponse response = githubClient.requestGithubProfile(accessToken);

        assertSoftly(softly -> {
            softly.assertThat(response.getEmail()).isNotBlank();
            softly.assertThat(response.getAge()).isNotNull();
        });
    }
}

