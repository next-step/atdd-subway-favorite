package nextstep.member.unit;

import nextstep.member.application.GithubClient;
import nextstep.member.fake.ui.GithubResponses;
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
    void getAccessTokenFromGithub() {
        String accessToken = githubClient.getAccessTokenFromGithub(GithubResponses.사용자1.getCode());

        assertThat(accessToken).isNotBlank();
    }
}
