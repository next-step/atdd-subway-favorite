package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    @DisplayName("Bearer Auth : 정상")
    @Test
    void bearerAuth() {
        MemberSteps.회원_생성_요청(EMAIL, PASSWORD, 3);

        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Bearer Auth : invalid email")
    @Test
    void test1() {
        // given
        MemberSteps.회원_생성_요청(EMAIL, PASSWORD, 3);

        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청("invalid", PASSWORD);

        // then
        assertThat(response.body().asString()).contains("존재하지 않는 Email");
    }

    @DisplayName("Bearer Auth : invalid password")
    @Test
    void test2() {
        // given
        MemberSteps.회원_생성_요청(EMAIL, PASSWORD, 3);

        // when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, "invalid");

        // then
        assertThat(response.body().asString()).contains("잘못된 비밀번호");
    }


}