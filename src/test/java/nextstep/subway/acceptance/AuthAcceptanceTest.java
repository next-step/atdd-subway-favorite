package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.JWT_토큰으로_내_회원_정보_조회_요청;
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

    @DisplayName("내 정보 조회가 가능하다")
    @Test
    void getMyInfo() {
        ExtractableResponse<Response> response = JWT_토큰으로_내_회원_정보_조회_요청(EMAIL, PASSWORD);

        assertThat(response.jsonPath().getLong("id")).isNotNull();
        assertThat(response.jsonPath().getString("email")).isEqualTo(EMAIL);
        assertThat(response.jsonPath().getLong("age")).isNotNull().isEqualTo(20);
    }
}
