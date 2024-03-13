package nextstep.auth.application;

import nextstep.auth.AuthenticationException;
import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.utils.GithubResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @Test
    void 깃헙_토큰_발급_성공() {
        String code = GithubResponse.회원.getCode();

        String githubToken = githubClient.requestGithubToken(code);

        assertThat(githubToken).isNotBlank();
        assertThat(githubToken).isEqualTo(GithubResponse.회원.getAccessToken());
    }

    @Test
    void 깃헙_토큰이_잘못된_회원_토큰_발급_실패() {
        String code = GithubResponse.토큰_없는_회원.getCode();

        Assertions.assertThatThrownBy( () -> githubClient.requestGithubToken(code)).isInstanceOf(
                AuthenticationException.class
        );
    }

    @Test
    void 사용자_정보_반환() {
        String accessToken = GithubResponse.회원.getAccessToken();

        GithubProfileResponse response = githubClient.requestGithubProfile(accessToken);

        assertAll(
                () -> assertThat(response.getEmail()).isEqualTo(GithubResponse.회원.getEmail()),
                () -> assertThat(response.getAge()).isEqualTo(GithubResponse.회원.getAge())
        );
    }

    @Test
    void 사용자_정보_없는_경우_사용자_정보_반환_실패() {
        String accessToken = GithubResponse.사용자_정보_없음.getAccessToken();

        Assertions.assertThatThrownBy( () -> githubClient.requestGithubProfile(accessToken)).isInstanceOf(
                AuthenticationException.class
        );
    }

}
