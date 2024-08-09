package nextstep.member.application;

import nextstep.member.application.dto.ProfileResponse;
import nextstep.utils.GithubResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @DisplayName("[requestGithubAccessToken] 깃허브 토큰을 발급받는다.")
    @Test
    public void requestGithubAccessToken_success() {
        String code = GithubResponse.사용자1.getCode();
        String githubAccessToken = githubClient.requestGithubAccessToken(code);

        assertThat(githubAccessToken).isNotBlank();
        assertThat(githubAccessToken).isEqualTo(GithubResponse.사용자1.getAccessToken());

    }

    @DisplayName("[requestGithubAccessToken] 깃허브 사용자 정보를 가져온다. ")
    @Test
    public void requestGithubProfile() {
        String accessToken = GithubResponse.사용자1.getAccessToken();
        ProfileResponse profileResponse = githubClient.requestGithubProfile(accessToken);

        assertThat(profileResponse.getEmail()).isEqualTo(GithubResponse.사용자1.getEmail());
        assertThat(profileResponse.getAge()).isEqualTo(GithubResponse.사용자1.getAge());

    }
}


