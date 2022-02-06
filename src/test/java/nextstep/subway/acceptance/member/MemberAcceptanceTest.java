package nextstep.subway.acceptance.member;

import static nextstep.subway.acceptance.member.MemberSteps.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;

class MemberAcceptanceTest extends AcceptanceTest {
    /**
     *
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
     *
     * */
    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        final String EMAIL = "email@email.com";
        final String PASSWORD = "password";
        final int AGE = 20;

        // 생성
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(회원_생성_요청(EMAIL, PASSWORD, AGE));

        // 조회
        ExtractableResponse<Response> getResponse = 회원_정보_조회_요청(createResponse);
        회원_정보_조회됨(getResponse, EMAIL, AGE);

        // 수정
        final String CHANGE_EMAIL = "new" + EMAIL;
        final String CHANGE_PASSWORD = "new" + PASSWORD;
        ExtractableResponse<Response> editResponse = 회원_정보_수정_요청(createResponse, CHANGE_EMAIL, CHANGE_PASSWORD, AGE);
        회원_정보_수정됨(editResponse);

        // 삭제
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);
        회원_정보_삭제됨(deleteResponse);
    }

    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
    }
}
