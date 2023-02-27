package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final int AGE = 20;

    /**
     * given : 회원을 데이터를 생성 하고
     * when : 인증 로그인 요청을 하면
     * then : jwt accessToken을 받을 수 있다.
     */
    @DisplayName("Bearer Auth")
    @Test
    void bearerAuth() {
        //given
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        //when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        //then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }
}