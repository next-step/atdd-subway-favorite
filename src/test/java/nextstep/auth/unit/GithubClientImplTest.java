package nextstep.auth.unit;

import nextstep.auth.fake.GithubUsers;
import nextstep.auth.infrastructure.GithubClientImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GithubClientImplTest {

    @Autowired
    private GithubClientImpl githubClient;

    @Test
    @DisplayName("Fake Github 엑세스 토큰 발행")
    void getAccessTokenFromGithub() {
        // Given
        var 사용자1 = GithubUsers.사용자1;

        // When
        var response = githubClient.getAccessTokenFromGithub(사용자1.getCode());

        // Then
        assertThat(response.getAccessToken()).isEqualTo(사용자1.getAccessToken());
    }

    @Test
    @DisplayName("Fake Github 인증")
    void requestGithubProfile() {
        // Given
        var 사용자1 = GithubUsers.사용자1;

        // When
        var response = githubClient.requestGithubProfile(사용자1.getAccessToken());

        // Then
        assertThat(response.getEmail()).isEqualTo(사용자1.getEmail());
    }
}
