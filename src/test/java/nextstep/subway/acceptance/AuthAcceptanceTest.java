package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.DataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.토큰으로_회원_정보_조회_요청;
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

    @DisplayName("내 정보를 조회한다.")
    @Test
    void getMyInfo() {
        ExtractableResponse<Response> loginResponse = 베어러_인증_로그인_요청(DataLoader.EMAIL, DataLoader.PASSWORD);

        ExtractableResponse<Response> response = 토큰으로_회원_정보_조회_요청(loginResponse);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("email")).isEqualTo(DataLoader.EMAIL);
        assertThat(response.jsonPath().getInt("age")).isEqualTo(DataLoader.AGE);
    }
}