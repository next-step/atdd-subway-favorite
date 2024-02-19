package nextstep.subway.member.client;

import nextstep.subway.member.client.config.GithubClientProperties;
import nextstep.subway.testhelper.fixture.GithubResponsesFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GithubClientTest {

    private GithubClient githubClient;

    @BeforeEach
    void setUp() {
        GithubClientProperties githubClientProperties = new GithubClientProperties();
        githubClientProperties.setClientId("client_id");
        githubClientProperties.setClientSecret("client_secret");
        githubClientProperties.setRootUrl("http://localhost:8080");
        githubClient = new GithubClient(githubClientProperties);
    }

    @Test
    @DisplayName("Github에 토큰 조회를 요청 할 수 있다.")
    void requestGithubToken() {
        String accessToken = githubClient.requestToken(GithubResponsesFixture.사용자1.getCode());

        assertAll(
                () -> assertThat(accessToken).isNotBlank(),
                () -> assertThat(accessToken).isEqualTo(GithubResponsesFixture.사용자1.getAccessToken())
        );
    }
}
