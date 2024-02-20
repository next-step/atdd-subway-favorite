package nextstep.auth.application;

import nextstep.auth.client.github.GithubClient;
import nextstep.auth.client.github.GithubTokenFetcher;
import nextstep.auth.client.github.config.GithubClientProperties;
import nextstep.subway.SubwayApplication;
import nextstep.subway.testhelper.fixture.GithubResponsesFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest(classes = SubwayApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GithubTokenProviderTest {

    private GithubTokenProvider githubTokenProvider;

    @BeforeEach
    void setUp() {
        GithubClientProperties githubClientProperties = new GithubClientProperties();
        githubClientProperties.setClientId("client_id");
        githubClientProperties.setClientSecret("client_secret");
        githubClientProperties.setRootUrl("http://localhost:8080");
        GithubClient githubClient = new GithubClient(githubClientProperties);
        GithubTokenFetcher githubTokenFetcher = new GithubTokenFetcher(githubClient);
        githubTokenProvider = new GithubTokenProvider(githubTokenFetcher);
    }

    @Test
    @DisplayName("토큰 조회를 요청 할 수 있다.")
    void createToken() {
        String actual = githubTokenProvider.createToken(GithubResponsesFixture.사용자1.getCode());
        String expected = GithubResponsesFixture.사용자1.getAccessToken();

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("사용자 정보를 요청 할 수 있다.")
    void getPrincipal() {
        String accessToken = githubTokenProvider.createToken(GithubResponsesFixture.사용자1.getCode());

        String actual = githubTokenProvider.getPrincipal(accessToken);
        String expected = GithubResponsesFixture.사용자1.getEmail();

        assertThat(actual).isEqualTo(expected);
    }

}
