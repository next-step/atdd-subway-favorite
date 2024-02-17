package nextstep.member.application;

import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubEmailResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.member.application.GithubResponses.사용자1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @Test
    void 깃허브_인증서버에_토큰을_요청한다() {
        GithubAccessTokenResponse response = githubClient.requestAccessToken(사용자1.getCode());

        String accessToken = response.getAccessToken();
        assertAll(
            () -> assertThat(accessToken).isNotBlank(),
            () -> assertThat(accessToken).isEqualTo(사용자1.getAccessToken())
        );
    }

    @Test
    void 깃허브_리소스서버에_이메일정보를_요청한다() {
        GithubEmailResponse response = githubClient.requestGithubEmail(사용자1.getAccessToken());

        String email = response.getEmail();
        assertAll(
                () -> assertThat(email).isNotBlank(),
                () -> assertThat(email).isEqualTo(사용자1.getEmail())
        );
    }
}
