package nextstep.member.application;

import nextstep.member.application.dto.GithubAccessTokenResponse;
import nextstep.member.application.dto.GithubClientProperties;
import nextstep.member.application.dto.GithubUrlProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static nextstep.member.application.GithubResponses.사용자1;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class GithubClientTest {

    private GithubClientProperties githubClientProperties;
    private GithubUrlProperties githubUrlProperties;

    @BeforeEach
    void setUp() {
        githubClientProperties = GithubClientProperties.builder()
                .id("client_id")
                .secret("client_secret")
                .build();

        githubUrlProperties = GithubUrlProperties.builder()
                .accessToken("https://github.com/login/oauth/access_token")
                .profile("https://api.github.com/user")
                .build();
    }

    @Test
    void 깃허브_인증서버에_토큰을_요청한다() {
        GithubClient githubClient = new GithubClient(githubClientProperties, githubUrlProperties);

        GithubAccessTokenResponse response = githubClient.requestAccessToken(사용자1.getCode());

        String accessToken = response.getAccessToken();
        assertAll(
            () -> assertThat(accessToken).isNotBlank(),
            () -> assertThat(accessToken).isEqualTo(사용자1.getAccessToken())
        );
    }
}
