package nextstep.auth.application;

import nextstep.exception.AuthenticationException;
import nextstep.auth.application.dto.GithubAccessTokenResponse;
import nextstep.auth.application.dto.GithubEmailResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.auth.application.GithubResponses.사용자1;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @Test
    void 성공_깃허브_인증서버에_토큰을_요청한다() {
        GithubAccessTokenResponse response = githubClient.requestAccessToken(사용자1.getCode());

        String accessToken = response.getAccessToken();
        assertAll(
            () -> assertThat(accessToken).isNotBlank(),
            () -> assertThat(accessToken).isEqualTo(사용자1.getAccessToken())
        );
    }

    @Test
    void 실패_깃허브_인증서버에_토큰을_가져오는데_실패할_경우_예외가_발생한다() {
        assertThatThrownBy(() -> githubClient.requestAccessToken(""))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("토큰 정보를 가져오는데 실패했습니다.");
    }

    @Test
    void 성공_깃허브_리소스서버에_이메일정보를_요청한다() {
        GithubEmailResponse response = githubClient.requestGithubEmail(사용자1.getAccessToken());

        String email = response.getEmail();
        assertAll(
                () -> assertThat(email).isNotBlank(),
                () -> assertThat(email).isEqualTo(사용자1.getEmail())
        );
    }

    @Test
    void 실패_깃허브_리소스_서버에_이메일_정보를_가져오는데_실패할_경우_예외가_발생한다() {
        assertThatThrownBy(() -> githubClient.requestGithubEmail(""))
                .isInstanceOf(AuthenticationException.class)
                .hasMessage("이메일 정보를 가져오는데 실패했습니다.");
    }
}
