package nextstep.member.unit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import nextstep.member.application.GithubClient;
import nextstep.member.application.OAuth2Client;
import nextstep.test.GithubResponses;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubClientTest {

    @LocalServerPort
    private int port;


    @Test
    void requestAccessToken() {
        // given
        String code = GithubResponses.사용자1.getCode();
        String clientId = "clientId";
        String clientSecret = "clientSecret";
        String accessTokenUri = "http://localhost:" + port + "/github/login/oauth/access_token";

        OAuth2Client githubClient = new GithubClient(clientId, clientSecret, accessTokenUri);
        // when
        String accessToken = githubClient.requestAccessToken(code);
        // then
        assertThat(accessToken).isNotBlank();
    }
}
