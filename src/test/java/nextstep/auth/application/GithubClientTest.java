package nextstep.auth.application;

import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {
    @Autowired
    private GithubClient githubClient;

    @DisplayName("code로 깃헙 토큰을 요청 한다.")
    @Test
    void requestGithubToken() {
        final String code = GithubResponses.사용자1.code();

        String githubToken = githubClient.requestGithubToken(code);

        assertThat(githubToken).isNotBlank();
        assertThat(githubToken).isEqualTo(GithubResponses.사용자1.accessToken());
    }

    @DisplayName("깃헙토큰으로 Profile을 요청 한다.")
    @Test
    void requestGithubProfile() {
        final String accessToken = GithubResponses.사용자1.accessToken();

        GithubProfileResponse response = githubClient.requestGithubProfile(accessToken);

        assertThat(response.getEmail()).isEqualTo(GithubResponses.사용자1.email());
    }
}

