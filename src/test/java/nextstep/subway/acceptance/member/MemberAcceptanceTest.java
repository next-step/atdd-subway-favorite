package nextstep.subway.acceptance.member;

import static nextstep.subway.acceptance.auth.AuthStep.*;
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
     * Feature: 나의 회원 정보를 관리한다.
     *
     *   Scenario: 나의 회원 정보를 관리
     *     Given 회원이 생성 되어있고
     *     And   로그인 되어 있다.
     *
     *     When 나의 회원 정보 수정 요청
     *     Then 나의 회원 정보 수정됨
     *     When 나의 회원 탈퇴 요청
     *     Then 나의 회원 탈퇴됨
     *
     * */
    @DisplayName("나의 정보를 관리한다.")
    @Test
    void manageMyInfo() {
        // Given
        String accessToken = 회원_생성_하고_로그인_됨(EMAIL, PASSWORD, AGE);

        // 수정
        final String CHANGE_EMAIL = "new" + EMAIL;
        final String CHANGE_PASSWORD = "new" + PASSWORD;
        내_회원_정보_수정_됨(accessToken, CHANGE_EMAIL, CHANGE_PASSWORD, AGE);

        // 탈퇴
        회원_탈퇴_됨(accessToken);
    }

    /**
     *
     * Feature: 나의 회원 정보 관리를 실패한다.
     *
     *   Scenario: 로그인 하지 않은채로 나의 회원 정보를 관리
     *     When 회원 정보 수정 요청
     *     When 회원 정보 수정 실패
     *     When 회원 탈퇴 요청
     *     Then 회원 탈퇴 실패
     *
     * */
    @DisplayName("나의 정보를 관리한다. - 권한이 없을 경우")
    @Test
    void manageMyInfoFailCase() {
        // 수정
        final String CHANGE_EMAIL = "new" + EMAIL;
        final String CHANGE_PASSWORD = "new" + PASSWORD;
        권한_없음(
            내_회원_정보_수정_됨("", CHANGE_EMAIL, CHANGE_PASSWORD, AGE)
        );

        // 탈퇴
        권한_없음(
            회원_탈퇴_됨("")
        );
    }
}
