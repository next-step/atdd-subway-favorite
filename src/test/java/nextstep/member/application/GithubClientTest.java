package nextstep.member.application;

import nextstep.utils.GithubResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

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

}
