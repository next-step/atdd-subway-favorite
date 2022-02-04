package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static nextstep.subway.acceptance.MemberSteps.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class AuthAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final Integer AGE = 20;

    /**
     * Feature: 회원 정보를 관리한다.
     *
     *   Scenario: 회원 정보를 관리
     *     When 회원 생성을 요청
     *     Then 회원 생성됨
     *     When 회원 정보 조회 요청
     *     Then 회원 정보 조회됨
     *     When 회원 정보 수정 요청
     *     Then 회원 정보 수정됨
     *     When 회원 삭제 요청
     *     Then 회원 삭제됨
     */
    @DisplayName("Session 로그인 후 내 정보 조회")
    @Test
    void myInfoWithSession() {
        //when
        ExtractableResponse<Response> 회원 = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        //then
        회원_생성_됨(회원);

        //when
        ExtractableResponse<Response> 내_회원_정보_조회_요청 = 내_회원_정보_조회_요청(EMAIL, PASSWORD);

        //then
        회원_정보_조회됨(내_회원_정보_조회_요청, EMAIL, AGE);
        
        //given
        String nextEmail = "icraft2170@gmail.com";
        String nextPassword = "icraft2170^@^";
        int nextAge = 30;

        //when
        ExtractableResponse<Response> 회원_정보_수정_요청 = 회원_정보_수정_요청(회원, nextEmail, nextPassword, nextAge);

        //then
        회원_수정_됨(회원_정보_수정_요청);

        //when
        ExtractableResponse<Response> 회원_삭제_요청 = 회원_삭제_요청(회원);

        //then
        회원_삭제_됨(회원_삭제_요청);
    }

    @DisplayName("Bearer Auth")
    @Test
    void myInfoWithBearerAuth() {
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        ExtractableResponse<Response> response = 내_회원_정보_조회_요청(accessToken);

        회원_정보_조회됨(response, EMAIL, AGE);
    }
}
