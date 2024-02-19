package nextstep.subway.member.client;

import nextstep.subway.testhelper.fixture.GithubResponsesFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class GithubClientTest {

    private GithubClient githubClient;

    @BeforeEach
    void setUp() {
        githubClient = new GithubClient();
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
