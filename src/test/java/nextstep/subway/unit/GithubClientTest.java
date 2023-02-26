package nextstep.subway.unit;

import nextstep.member.application.GithubClient;
import nextstep.member.domain.exception.AuthorizationException;
import nextstep.subway.utils.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ActiveProfiles("test")
@TestPropertySource("classpath:/application-test.properties")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class GithubClientTest {
    @Autowired
    private GithubClient githubClient;

    @DisplayName("Github access-token 발급")
    @EnumSource(value = GithubResponses.class)
    @ParameterizedTest
    void getAccessTokenFromGithub(GithubResponses responses) {
        // when
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(responses.getCode());

        // then
        assertThat(accessTokenFromGithub).isEqualTo(responses.getAccessToken());
    }

    @DisplayName("Github에서 받은 권한증서가 공백이라면, GitHub Access Token 발급 요청.")
    @Test
    void getAccessTokenFromGithub_WithNullCode() {
        // given
        String code = null;

        // when
        // then
        assertThatThrownBy(() -> githubClient.getAccessTokenFromGithub(code))
                .isInstanceOf(AuthorizationException.class);
    }
}
