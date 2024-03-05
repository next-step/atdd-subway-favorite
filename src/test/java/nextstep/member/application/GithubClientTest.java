package nextstep.member.application;

import nextstep.member.application.dto.OAuth2ProfileResponse;
import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.member.acceptance.GithubResponses.사용자1;
import static org.assertj.core.api.Assertions.*;

@ActiveProfiles("test")
public class GithubClientTest extends AcceptanceTest {
    @Autowired
    private GithubClient githubClient;

    @DisplayName("깃허브 accessToken 요청")
    @Test
    void requestGithubToken() {
        String accessToken = githubClient.requestGithubToken(사용자1.getCode());

       assertThat(accessToken).isEqualTo(사용자1.getAccessToken());
    }

    @DisplayName("깃허브 profile 요청")
    @Test
    void requestGithubProfile() {
        OAuth2ProfileResponse res = githubClient.requestGithubProfile(사용자1.getAccessToken());

        assertThat(res.getEmail()).isEqualTo(사용자1.getEmail());
        assertThat(res.getAge()).isEqualTo(사용자1.getAge());
    }
}
