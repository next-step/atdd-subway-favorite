package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.config.GithubOauthConfig;
import nextstep.subway.config.MockGithubResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;

import java.util.stream.Stream;

import static nextstep.subway.acceptance.AuthSteps.깃허브로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Import(GithubOauthConfig.class)
class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    // Given 깃허브 로그인을 요청하고
    // When Github에서 권한 인증 코드를 요청하면
    // Then 애플리케이션 accessToken 응답에 성공한다
    @DisplayName("깃허브로그인 요청에 애플리케이션 accesstoken을 발급한다")
    @ParameterizedTest
    @MethodSource("provideRequestUsers")
    void 깃허브로그인_요청에_애플리케이션_accesstoken을_발급한다(MockGithubResponse mockGithubResponse) {
        ExtractableResponse<Response> response = 깃허브로그인_요청(mockGithubResponse.getCode());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(response.jsonPath().getString("accessToken")).isNotBlank()
        );
    }

    private static Stream<Arguments> provideRequestUsers() {
        return Stream.of(
                Arguments.of(MockGithubResponse.사용자1),
                Arguments.of(MockGithubResponse.사용자2),
                Arguments.of(MockGithubResponse.사용자3),
                Arguments.of(MockGithubResponse.사용자3)
        );
    }
}
