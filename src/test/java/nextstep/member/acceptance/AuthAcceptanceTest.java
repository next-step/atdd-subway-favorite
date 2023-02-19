package nextstep.member.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.member.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.member.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";

    /**
     * Given 회원가입을 하고
     * When 베어러 인증 로그인 요청을 하면
     * Then 액세스 토큰을 발급 받는다.
     */
    @DisplayName("베어러 인증 로그인 성공")
    @Test
    void bearerAuth() {
        //given
        ExtractableResponse<Response> memberResponse = 회원_생성_요청(EMAIL, PASSWORD, 20);

        //when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * When 존재하지 않는 회원 정보로 베어러 인증 로그인 요청을 하면
     * Then 응답 코드 401를 받고 엑세스 토큰 발급을 실패한다.
     */
    @DisplayName("베어러 인증 로그인 실패")
    @Test
    void bearerAuthFail() {
        //when
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}