package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static nextstep.subway.acceptance.MemberSteps.내_회원_정보_조회_요청;
import static nextstep.subway.acceptance.MemberSteps.내_회원_정보_조회_요청_토큰_비인증;
import static nextstep.subway.acceptance.MemberSteps.로그인_되어_있음;
import static nextstep.subway.acceptance.MemberSteps.회원_생성_요청;
import static nextstep.subway.acceptance.MemberSteps.회원_정보_조회됨;


class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(EMAIL, PASSWORD);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, EMAIL, AGE);
    }

    @DisplayName("Bearer Auth 비로그인 예외")
    @Test
    void exception_BearerAuth() {
        ExtractableResponse<Response> response = 내_회원_정보_조회_요청_토큰_비인증();

        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}
