package nextstep.auth.acceptance.unit;

import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.application.oauth.GithubClient;
import nextstep.auth.ui.dto.TokenFromGithubRequestBody;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static nextstep.utils.fixture.GithubUserFixture.사용자1;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {
    @Autowired
    GithubClient githubClient;

    @Test
    @DisplayName("토큰 요청 정상 동작")
    void succeedToGetToken() {
        TokenFromGithubRequestBody request = new TokenFromGithubRequestBody(사용자1.getCode());

        String token = githubClient.requestToken(request);
        assertThat(token).isEqualTo(사용자1.getAccessToken());
    }

    @Test
    @DisplayName("유저 정보 요청 정상 동작")
    void succeedToGetProfile() {
        String token = 사용자1.getAccessToken();

        GithubProfileResponse profileResponse = githubClient.requestProfile(token);

        assertThat(profileResponse.getEmail()).isEqualTo(사용자1.getEmail());
    }
}
