package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;


class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @BeforeEach
    void 회원_생성_준비() {
        super.setUp();
        회원_생성_요청(EMAIL, PASSWORD, AGE);
    }

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(response, EMAIL, AGE);
    }


    @DisplayName("존재하지 않는 회원 정보 조회 요청시 Session에 정보 존재하지 않아 실패 (401)")
    @Test
    void myInfoWithSessionFailure() {
        내_회원_정보_조회_요청_권한_없음_실패("unknown", "unknown");
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("존재하지 않는 회원 정보 토큰 조회 요청시 실패 (401)")
    @Test
    void myInfoWithBearerAuthFailure() {
        ExtractableResponse<Response> response = 토큰_로그인_요청("unknown", "unknown");

        assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
