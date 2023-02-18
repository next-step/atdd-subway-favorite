package nextstep.subway.unit;

import static nextstep.subway.utils.GitHubResponses.*;
import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import nextstep.member.application.GitHubClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class GitHubClientTest {

    @Autowired
    private GitHubClient gitHubClient;

    @DisplayName("권한증서로 GitHub Access Token을 발급한다.")
    @MethodSource("getAccessTokenFromGithubSource")
    @ParameterizedTest
    void getAccessTokenFromGithub(String code, String accessToken) {
        // when
        String accessTokenFromGitHub = gitHubClient.getAccessTokenFromGithub(code);

        // then
        assertThat(accessTokenFromGitHub).isEqualTo(accessToken);
    }

    private static Stream<Arguments> getAccessTokenFromGithubSource() {
        return Stream.of(
            Arguments.of(사용자1.getCode(), 사용자1.getAccessToken()),
            Arguments.of(사용자2.getCode(), 사용자2.getAccessToken()),
            Arguments.of(사용자3.getCode(), 사용자3.getAccessToken()),
            Arguments.of(사용자4.getCode(), 사용자4.getAccessToken())
        );
    }

    @DisplayName("권한증서가 null 또는 공백이라면, GitHub Access Token 발급 요청 시 예외가 발생한다.")
    @NullAndEmptySource
    @ParameterizedTest
    void cannotGetAccessTokenFromGitHub(String code) {
        // when & then
        assertThatThrownBy(() -> gitHubClient.getAccessTokenFromGithub(code))
            .isInstanceOf(HttpClientErrorException.class);
    }
}
