package nextstep.auth;

import nextstep.auth.application.GithubClient;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.auth.GithubResponse.사용자1;
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

    @DisplayName("깃헙 프로필 요청")
    @Test
    void requestGithubProfile() {
        GithubProfileResponse githubProfileResponse = githubClient.requestGithubProfile(사용자1.getAccessToken());

        assertThat(githubProfileResponse.getEmail()).isEqualTo(사용자1.getEmail());
    }
}
