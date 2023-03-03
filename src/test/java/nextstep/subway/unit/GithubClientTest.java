package nextstep.subway.unit;

import nextstep.common.exception.AuthorizationException;
import nextstep.member.application.GithubClient;
import nextstep.member.application.dto.GithubProfileResponse;
import nextstep.subway.ApplicationContextTest;
import nextstep.subway.utils.GithubResponses;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.beans.factory.annotation.Autowired;

import static nextstep.subway.utils.GithubResponses.사용자1;
import static nextstep.subway.utils.GithubResponses.사용자5;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

public class GithubClientTest extends ApplicationContextTest {
    @Autowired
    private GithubClient githubClient;

    @DisplayName("Github access-token 발급")
    @EnumSource(value = GithubResponses.class, mode = EXCLUDE, names = {"사용자5"})
    @ParameterizedTest
    void getAccessTokenFromGithub(GithubResponses responses) {
        // when
        String accessTokenFromGithub = githubClient.getAccessTokenFromGithub(responses.getCode());

        // then
        assertThat(accessTokenFromGithub).isEqualTo(responses.getAccessToken());
    }

    @DisplayName("Github 에서 받은 권한증서가 null 이라면, GitHub Access Token 발급 요청.")
    @Test
    void getAccessTokenFromGithub_WithNullCode() {
        // given
        String code = null;

        // when
        // then
        assertThatThrownBy(() -> githubClient.getAccessTokenFromGithub(code))
                .isInstanceOf(AuthorizationException.class);
    }

    @DisplayName("Github 에서 받은 AccessToken 이 null 일 경우")
    @Test
    void getAccessTokenFromGithub_WithNullAccessToken() {
        // when
        // then
        assertThatThrownBy(() -> githubClient.getAccessTokenFromGithub(사용자5.getCode()))
                .isInstanceOf(RuntimeException.class);
    }

    @DisplayName("AccessToken 으로 Github 에서 사용자 프로필을 조회")
    @Test
    void getGithubProfileFromGitHub() {
        // given
        String accessToken = githubClient.getAccessTokenFromGithub(사용자1.getCode());

        // when
        GithubProfileResponse githubProfile = githubClient.getGithubProfileFromGithub(accessToken);

        // then
        assertThat(githubProfile.getEmail()).isEqualTo(사용자1.getEmail());
    }

    @DisplayName("인증되지 않은 AccessToken 으로 조회")
    @Test
    void githubProfileNotFoundFromGitHub() {
        // when & then
        Assertions.assertThatThrownBy(() -> githubClient.getGithubProfileFromGithub("access token"))
                .isInstanceOf(AuthorizationException.class);
    }
}
