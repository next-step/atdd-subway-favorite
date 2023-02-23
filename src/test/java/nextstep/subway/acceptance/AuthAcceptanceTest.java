package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.베어러_인증_로그인_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "admin@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;
    private String INVALID_EMAIL = "admin2@email.com";
    private String INVALID_PASSWORD = "password2";

    /**
     * Given 회원을 생성한다.
     * When 베어러 인증으로 토큰을 생성한다.
     * Then 토큰을 확인한다.
     */
    @DisplayName("Bearer Auth 정상 실행")
    @Test
    void bearerAuth() {
        //Given
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        //When
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, PASSWORD);

        //Then
        assertThat(response.jsonPath().getString("accessToken")).isNotBlank();
    }

    /**
     * Given 회원을 생성한다.
     * When 잘못된 이메일로 토큰 생성을 요청한다.
     * Then 예외가 발생한다.
     */
    @DisplayName("Bear Auth 이메일 오입력시")
    @Test
    void bearerAuth2() {
        //Given
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        //When
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(INVALID_EMAIL, PASSWORD);

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    /**
     * Given 회원을 생성한다.
     * When 잘못된 비밀번호로 토큰 생성을 요청한다.
     * Then 예외가 발생한다.
     */
    @DisplayName("Bear Auth 비밀번호 오입력시")
    @Test
    void bearerAuth3() {
        //Given
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        //When
        ExtractableResponse<Response> response = 베어러_인증_로그인_요청(EMAIL, INVALID_PASSWORD);

        //Then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}