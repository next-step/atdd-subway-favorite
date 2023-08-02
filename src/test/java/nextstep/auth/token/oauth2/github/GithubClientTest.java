package nextstep.auth.token.oauth2.github;

import nextstep.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class GithubClientTest extends AcceptanceTest {

    @Autowired
    private GithubClient githubClient;

    @DisplayName("Github Auth - 토큰 발급을 깃 허브 테스트 컨트롤러에서 받아온다.")
    @Test
    void getAccessToken() {
        // given : 선행조건 기술

        // when : 기능 수행
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub("code");

        // then : 결과 확인
        assertThat(accessTokenFromGithub).isNotBlank();
    }

    @Test
    void getGithubProfileFromGithub() {
        // given : 선행조건 기술
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub("code");

        // when : 기능 수행
        GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(accessTokenFromGithub);

        // then : 결과 확인
        assertThat(githubProfile).isNotNull();
    }
}