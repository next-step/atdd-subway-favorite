package nextstep.subway.acceptance.member;

import static nextstep.subway.acceptance.member.MemberSteps.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import nextstep.subway.acceptance.AcceptanceTest;

class MemberAcceptanceTest extends AcceptanceTest {
    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private final int AGE = 20;

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
        // 생성
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);
        회원_생성됨(회원_생성_요청(EMAIL, PASSWORD, AGE));

        // 조회
        ExtractableResponse<Response> getResponse = 회원_정보_조회_요청(createResponse);
        회원_정보_조회됨(getResponse, EMAIL, AGE);

        // 수정
        final String CHANGE_EMAIL = "new" + EMAIL;
        final String CHANGE_PASSWORD = "new" + PASSWORD;
        회원_정보_수정_됨(createResponse, CHANGE_EMAIL, CHANGE_PASSWORD, AGE);

        // 삭제
        회원_삭제_됨(createResponse);
    }

    /**
     *
     *
     * */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // 생성, 로그인
        String token = 회원_생성_하고_로그인_됨(EMAIL, PASSWORD, AGE);

        // 수정
        final String CHANGE_EMAIL = "new" + EMAIL;
        final String CHANGE_PASSWORD = "new" + PASSWORD;
        내_회원_정보_수정_됨(token, CHANGE_EMAIL, CHANGE_PASSWORD, AGE);

        // 탈퇴
        회원_탈퇴_됨(token);
    }
}
