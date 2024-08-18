package nextstep.member.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import nextstep.member.domain.dto.GithubProfileResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@DisplayName("깃허브 클라이언트 테스트")
class GithubClientTest {

    private GithubClient githubClient;

    @BeforeEach
    void init() {
        githubClient = new GithubClient("github-client-id", "github-client-secret",
                "http://localhost:8080/github/login/oauth/access_token", "http://localhost:8080/github/user");
    }

    @DisplayName("Access Token 획득")
    @Test
    public void getAccessToken() {
        // When
        String accessToken = githubClient.getAccessToken(GithubResponses.사용자2.getCode());

        // Then
        assertThat(accessToken).isEqualTo("access_token_2");
    }

    @DisplayName("github 프로필 요청")
    @Test
    void requestProfile() {
        // when
        String accessToken = githubClient.getAccessToken(GithubResponses.사용자1.getCode());
        GithubProfileResponse response = githubClient.requestProfile(accessToken);

        // then
        assertThat(accessToken).isEqualTo(GithubResponses.사용자1.getAccessToken());
        assertThat(response.getEmail()).isEqualTo(GithubResponses.사용자1.getEmail());
    }
}