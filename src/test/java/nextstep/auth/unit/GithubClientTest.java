package nextstep.auth.unit;

import nextstep.auth.GithubResponse;
import nextstep.auth.application.GithubClient;
import nextstep.auth.application.dto.GithubProfileResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {
    @Autowired
    private GithubClient githubClient;

    @DisplayName("깃헙 토큰 요청")
    @ParameterizedTest
    @EnumSource(value = GithubResponse.class)
    void requestGithubToken(GithubResponse githubResponse) {
        String code = githubResponse.getCode();

        String accessCode = githubClient.requestGithubToken(code);

        assertThat(accessCode).isEqualTo(githubResponse.getAccessToken());
    }

    @DisplayName("깃헙 프로필 요청")
    @ParameterizedTest
    @EnumSource(value = GithubResponse.class)
    void requestGithubProfile(GithubResponse githubResponse) {
        String code = githubResponse.getCode();
        String accessToken = githubClient.requestGithubToken(code);

        GithubProfileResponse response = githubClient.requestGithubProfile(accessToken);

        assertThat(response.getEmail()).isEqualTo(githubResponse.getEmail());
        assertThat(response.getAge()).isEqualTo(githubResponse.getAge());
    }
}
