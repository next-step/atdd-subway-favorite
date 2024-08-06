package nextstep.member.unit;

import static org.assertj.core.api.Assertions.assertThat;

import nextstep.member.application.GithubClient;
import nextstep.member.GithubResponses;
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
}
