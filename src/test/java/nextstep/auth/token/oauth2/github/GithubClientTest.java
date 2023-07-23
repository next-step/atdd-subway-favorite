package nextstep.auth.token.oauth2.github;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GithubClientTest {

    @Autowired
    private GithubClient githubClient;

    @Test
    void getAccessTokenFromGithub() {
        String code = githubClient.getAccessTokenFromGithub("code");
        System.out.println(code);
    }

}