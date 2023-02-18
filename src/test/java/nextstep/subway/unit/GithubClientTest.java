package nextstep.subway.unit;

import nextstep.auth.application.dto.GithubProfileResponse;
import nextstep.auth.config.exception.MissingTokenException;
import nextstep.auth.infra.GithubClient;
import nextstep.subway.acceptance.ApplicationContextTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Stream;

import static nextstep.auth.config.message.AuthError.NOT_MISSING_TOKEN;
import static nextstep.subway.utils.GithubResponses.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Github 인증 관련 테스트")
class GithubClientTest extends ApplicationContextTest {

    @Autowired
    private GithubClient githubClient;

    @DisplayName("권한증서로 GitHub Access Token 을 발급한다.")
    @MethodSource("providerAccessTokenFromGitHubSource")
    @ParameterizedTest
    void getAccessTokenFromGithub(final String code, final String accessToken) {
        final String accessTokenFromGitHub = githubClient.getAccessTokenFromGithub(code);

        assertThat(accessTokenFromGitHub).isEqualTo(accessToken);
    }

    private static Stream<Arguments> providerAccessTokenFromGitHubSource() {
        return Stream.of(
                Arguments.of(USER1.getCode(), USER1.getAccessToken()),
                Arguments.of(USER2.getCode(), USER2.getAccessToken()),
                Arguments.of(USER3.getCode(), USER3.getAccessToken()),
                Arguments.of(USER4.getCode(), USER4.getAccessToken())
        );
    }

    @DisplayName("권한증서가 null 또는 공백이라면, GitHub Access Token 발급 요청 시 예외가 발생한다.")
    @NullAndEmptySource
    @ParameterizedTest
    void cannotGetAccessTokenFromGitHub(final String code) {
        assertThatThrownBy(() -> githubClient.getAccessTokenFromGithub(code))
                .isInstanceOf(MissingTokenException.class)
                .hasMessageContaining(NOT_MISSING_TOKEN.getMessage());
    }

    @DisplayName("Access Token 으로 GitHub 에서 사용자 프로필을 조회한다.")
    @MethodSource("providerEmailFromGitHubSource")
    @ParameterizedTest
    void getGithubProfileFromGitHub(final String accessToken, final String email) {
        final GithubProfileResponse githubProfileResponse = githubClient.getGithubProfileFromGithub(accessToken);

        assertThat(githubProfileResponse.getEmail()).isEqualTo(email);
    }

    private static Stream<Arguments> providerEmailFromGitHubSource() {
        return Stream.of(
                Arguments.of(USER1.getAccessToken(), USER1.getEmail()),
                Arguments.of(USER2.getAccessToken(), USER2.getEmail()),
                Arguments.of(USER3.getAccessToken(), USER3.getEmail()),
                Arguments.of(USER4.getAccessToken(), USER4.getEmail())
        );
    }

    @DisplayName("GitHub 사용자 프로필 조회 시, Access Token 에 해당하는 사용자가 없으면 예외가 발생한다.")
    @Test
    void githubProfileNotFoundFromGitHub() {
        assertThatThrownBy(() -> githubClient.getGithubProfileFromGithub("access token"))
                .isInstanceOf(RuntimeException.class);
    }
}
