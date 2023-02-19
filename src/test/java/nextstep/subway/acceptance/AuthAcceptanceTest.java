package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.utils.FakeGithubResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.깃허브_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @Nested
    @DisplayName("깃허브 인증")
    class github {

        @DisplayName("깃허브 인증 로그인 요청")
        @Test
        void githubAuth() {
            ExtractableResponse<Response> response = 깃허브_인증_로그인_요청(FakeGithubResponse.사용자1.getCode());
            assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
        }

        @DisplayName("깃허브 인증 로그인 요청 실패")
        @Test
        void githubAuthFail() {
            ExtractableResponse<Response> response = 깃허브_인증_로그인_요청("0000");
            assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        }
    }
}
