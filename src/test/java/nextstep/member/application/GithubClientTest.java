package nextstep.member.application;

import nextstep.member.application.dto.GithubProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @Test
    @DisplayName("requestGithubToken 를 통해 access_token 을 발급 받을 수 있다.")
    void requestGithubToken() {
        final String code = "code";

        final String githubToken = githubClient.requestGithubToken(code);

        assertThat(githubToken).isNotBlank();
    }

    @Test
    @DisplayName("requestGithubProfile 를 통해 user github profile 정보를 반환받을 수 있다.")
    void requestGithubProfile() {
        final String code = "code";

        final GithubProfileResponse profileResponse = githubClient.requestGithubProfile(code);

        assertSoftly(softly -> {
            softly.assertThat(profileResponse.getEmail()).isNotBlank();
            softly.assertThat(profileResponse.getAge()).isNotNull();
        });
    }

}
