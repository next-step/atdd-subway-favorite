package nextstep.auth.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import nextstep.auth.infrastructure.oauth.github.GithubClient;
import nextstep.auth.GithubResponses;
import nextstep.auth.infrastructure.oauth.github.dto.GithubProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @DisplayName("깃헙에서 토큰 가져오기 테스트")
    @Test
    void requestGithubAecessToken(){

        String code = GithubResponses.사용자1.getCode();
        String githubAccessToken = githubClient.requestGithubAeccessToken(code);

        assertThat(githubAccessToken).isNotBlank();
        assertThat(githubAccessToken).isEqualTo(GithubResponses.사용자1.getAccessToken());
    }

    @DisplayName("깃헙에서 사용자 정보 가져오기 테스트")
    @Test
    void requestGithubUser(){

        String code = GithubResponses.사용자2.getCode();
        String githubAccessToken = githubClient.requestGithubAeccessToken(code);

        GithubProfileResponse githubProfileResponse = githubClient.requestGithubUser(githubAccessToken);

        assertThat(githubProfileResponse.getEmail()).isEqualTo(GithubResponses.사용자2.getEmail());
    }
}
