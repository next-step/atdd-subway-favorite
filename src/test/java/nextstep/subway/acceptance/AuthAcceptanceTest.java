package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.*;


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

    // 리뷰 코멘트 질문 : AuthenticationPrincipalArgumentResolver 테스트 방법
    // AuthenticationPrincipalArgumentResolver 테스트 방법을 몰라 주석해 두었습니다.
//    @DisplayName("Bearer Auth 비로그인 예외")
//    @Test
//    void exception_BearerAuth() {
//        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(null);
//
//        회원_정보_조회됨(response, EMAIL, AGE);
//    }
}
