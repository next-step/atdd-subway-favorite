package nextstep.subway.acceptance;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.fake.FakeGithubResponses;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @ParameterizedTest(name = "Github Auth: {0}")
    @MethodSource("provideGithubCodes")
    void githubAuth(String code) {
        ExtractableResponse<Response> response = 깃허브_인증_로그인_요청(code);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    private static Stream<Arguments> provideGithubCodes() {
        return Stream.of(
            Arguments.of(FakeGithubResponses.사용자1.getCode()),
            Arguments.of(FakeGithubResponses.사용자2.getCode()),
            Arguments.of(FakeGithubResponses.사용자3.getCode()),
            Arguments.of(FakeGithubResponses.사용자4.getCode())
        );
    }
}
