package nextstep.member.unit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import nextstep.auth.application.GithubClient;
import nextstep.auth.application.OAuth2Client;
import nextstep.auth.ui.dto.GithubProfileResponse;
import nextstep.test.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GithubClientTest {

    @LocalServerPort
    private int port;

    private String clientId;
    private String clientSecret;
    private String accessTokenUri;
    private String userInfoUri;

    @BeforeEach
    void setUp() {
        clientId = "clientId";
        clientSecret = "clientSecret";
        accessTokenUri = "http://localhost:" + port + "/github/login/oauth/access_token";
        userInfoUri = "http://localhost:" + port + "/github/user";
    }

    @Test
    void requestAccessToken() {
        // given
        String code = GithubResponses.사용자1.getCode();

        OAuth2Client githubClient = new GithubClient(clientId, clientSecret, accessTokenUri,
            userInfoUri);
        // when
        String accessToken = githubClient.requestAccessToken(code);
        // then
        assertThat(accessToken).isNotBlank();
    }

    @Test
    void requestUserInfo() {
        // given
        String accessToken = GithubResponses.사용자1.getAccessToken();
        OAuth2Client githubClient = new GithubClient(clientId, clientSecret, accessTokenUri,
            userInfoUri);
        // when
        GithubProfileResponse response = githubClient.requestUserInfo(accessToken);

        // then
        assertThat(response.getEmail()).isNotBlank();
    }
}
