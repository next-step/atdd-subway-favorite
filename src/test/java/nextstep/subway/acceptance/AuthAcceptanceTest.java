package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.*;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 30;

    @DisplayName("Bearer Auth : Bearer 인증 테스트")
    @Test
    void bearerAuth() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    @DisplayName("Bearer Auth : Bearer 인증 실패 테스트")
    @Test
    void bearerAuthFailure() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);
        assertThatThrownBy(() -> 베어러_인증_로그인_요청(PASSWORD, EMAIL), new NullPointerException().getMessage());
    }
}