package nextstep.subway.unit;

import static nextstep.subway.utils.GitHubResponses.*;
import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;

import nextstep.exception.AuthorizationException;
import nextstep.member.application.GitHubClient;
import nextstep.member.application.dto.GitHubProfileResponse;
import nextstep.subway.acceptance.ApplicationContextTest;

class GitHubClientTest extends ApplicationContextTest {

    @Autowired
    private GitHubClient gitHubClient;

    @DisplayName("권한증서로 GitHub Access Token 을 발급한다.")
    @MethodSource("getAccessTokenFromGitHubSource")
    @ParameterizedTest
    void getAccessTokenFromGithub(String code, String accessToken) {
        // when
        String accessTokenFromGitHub = gitHubClient.getAccessTokenFromGitHub(code);

        // then
        assertThat(accessTokenFromGitHub).isEqualTo(accessToken);
    }

    private static Stream<Arguments> getAccessTokenFromGitHubSource() {
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
        assertThatThrownBy(() -> gitHubClient.getAccessTokenFromGitHub(code))
            .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("GitHub Access Token 발급 요청 시, 응답 결과에 Access Token 이 없으면 예외가 발생한다.")
    @Test
    void emptyAccessTokenFromGitHub() {
        // when
        String code = 사용자5_ACCESS_TOKEN_없음.getCode();

        // then
        assertThatThrownBy(() -> gitHubClient.getAccessTokenFromGitHub(code))
            .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("Access Token 으로 GitHub 에서 사용자 프로필을 조회한다.")
    @Test
    void getGithubProfileFromGitHub() {
        // when
        GitHubProfileResponse githubProfile = gitHubClient.getGithubProfileFromGithub(사용자1.getAccessToken());

        // then
        assertThat(githubProfile.getEmail()).isEqualTo(사용자1.getEmail());
    }

    @DisplayName("GitHub 사용자 프로필 조회 시, Access Token 에 해당하는 사용자가 없으면 예외가 발생한다.")
    @Test
    void githubProfileNotFoundFromGitHub() {
        // when & then
        assertThatThrownBy(() -> gitHubClient.getGithubProfileFromGithub("access token"))
            .isInstanceOf(AuthorizationException.class);
    }
}
