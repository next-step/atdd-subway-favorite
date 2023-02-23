package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import static nextstep.subway.acceptance.MemberSteps.깃헙_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.깃헙_인증_로그인_요청_실패;
import static nextstep.subway.acceptance.MemberSteps.리다이렉트_요청;
import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AuthAcceptanceTest extends AcceptanceTest {

    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final String CODE = "832ovnq039hfjn";

    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        회원_생성_요청(EMAIL, PASSWORD, 20);
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Github Auth 성공")
    @Test
    void githubAuth() {
        ExtractableResponse<Response> response = 깃헙_인증_로그인_요청(CODE);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Github Auth 예외 (계정이 없는 경우 회원가입 페이지로 redirect")
    @Test
    void githubAuthRedirect() {
        ExtractableResponse<Response> response = 깃헙_인증_로그인_요청_실패("testtesttest");

        ExtractableResponse<Response> response2 = 리다이렉트_요청(response);

        assertAll(() -> assertThat(response2.statusCode()).isEqualTo(HttpStatus.OK.value()),
                  () -> assertThat(response2.jsonPath().getString("accessToken")).isNotBlank());
    }
}