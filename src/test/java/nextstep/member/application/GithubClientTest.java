package nextstep.member.application;

import nextstep.utils.GithubResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class GithubClientTest {

    @DisplayName("1. 깃허브 토큰을 발급받는 테스트")
    @Test
    public void requestGithubAccessToken() {
        GithubClient githubClient = new GithubClient();

        String code = GithubResponse.사용자1.getCode();
        String githubAccessToken = githubClient.requestGithubAccessToken(code);

        assertThat(githubAccessToken).isNotBlank();
        assertThat(githubAccessToken).isEqualTo(GithubResponse.사용자1.getAccessToken());


    }

    // 2. 깃허브 사용자 정보를 가져오는 테스트

}


