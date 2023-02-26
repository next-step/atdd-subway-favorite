package nextstep.member.unit;

import nextstep.auth.domain.Oauth2Client;
import nextstep.auth.domain.ProfileResponse;
import nextstep.member.domain.FakeGithubClientImpl;
import nextstep.member.domain.GithubResponses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static nextstep.common.constants.ErrorConstant.INVALID_AUTHENTICATION_INFO;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class Oauth2ClientTest {

    Oauth2Client client;

    @BeforeEach
    void setUp() {
        // give
        client = new FakeGithubClientImpl();
    }

    @Test
    @DisplayName("액세스 토큰 조회 실패-잘못된 권한 증서")
    void getAccessToken_invalidCode() {
        // when
        // then
        assertThatThrownBy(() -> client.getAccessToken("code"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(INVALID_AUTHENTICATION_INFO);
    }

    @ParameterizedTest
    @MethodSource("validCodeAndExpectedAccessTokenParameter")
    @DisplayName("액세스 토큰 조회")
    void getAccessToken(final String code,
                        final String expectedAccessToken) {
        // when
        final String accessToken = client.getAccessToken(code);

        // then
        assertThat(accessToken).isEqualTo(expectedAccessToken);
    }

    @Test
    @DisplayName("자원 조회 실패-잘못된 액세스 토큰")
    void getProfile_invalidAccessToken() {
        // when
        // then
        assertThatThrownBy(() -> client.getProfile("accessToken"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage(INVALID_AUTHENTICATION_INFO);
    }

    @ParameterizedTest
    @MethodSource("validAccessTokenParameterAndExpectedEmailParameter")
    @DisplayName("자원 조회")
    void getProfile(final String accessToken,
                    final String expectedEmail) {
        // when
        final ProfileResponse profile = client.getProfile(accessToken);

        // then
        assertThat(profile.getEmail()).isEqualTo(expectedEmail);
    }

    private static Stream<Arguments> validCodeAndExpectedAccessTokenParameter() {
        return Stream.of(
                Arguments.of(GithubResponses.사용자1.getCode(), GithubResponses.사용자1.getAccessToken()),
                Arguments.of(GithubResponses.사용자2.getCode(), GithubResponses.사용자2.getAccessToken()),
                Arguments.of(GithubResponses.사용자3.getCode(), GithubResponses.사용자3.getAccessToken())
        );
    }

    private static Stream<Arguments> validAccessTokenParameterAndExpectedEmailParameter() {
        return Stream.of(
                Arguments.of(GithubResponses.사용자1.getAccessToken(), GithubResponses.사용자1.getEmail()),
                Arguments.of(GithubResponses.사용자2.getAccessToken(), GithubResponses.사용자2.getEmail()),
                Arguments.of(GithubResponses.사용자3.getAccessToken(), GithubResponses.사용자3.getEmail())
        );
    }
}
