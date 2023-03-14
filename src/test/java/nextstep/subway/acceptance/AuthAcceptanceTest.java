package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.fake.GithubResponses;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.깃헙_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private final String EMAIL = "admin@email.com";
    private final String PASSWORD = "password";

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Bearer Auth 실패 - 없는 이메일")
    @Test
    void bearerAuthWhenEmailDoesNotExist() {
        String email = "wrong@email.com";

        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(email, PASSWORD);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("Bearer Auth 실패 - pw 오류")
    @Test
    void bearerAuthWhenPWDoesNotMatch() {
        String pw = "wrongPassword";

        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, pw);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("Github Auth")
    @ParameterizedTest
    @EnumSource(GithubResponses.class)
    void githubAuth(GithubResponses user) {
        ExtractableResponse<Response> response = 깃헙_로그인_요청(user.getCode());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}