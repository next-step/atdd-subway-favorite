package nextstep.subway.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static nextstep.subway.acceptance.MemberSteps.*;

@DisplayName("회원 정보 관리")
class MemberAcceptanceTest extends AcceptanceTest {
    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";
    public static final int AGE = 20;

    /**
     * When 회원 생성을 요청
     * Then 회원 생성됨
     * When 회원 정보 조회 요청
     * Then 회원 정보 조회됨
     * When 회원 정보 수정 요청
     * Then 회원 정보 수정됨
     * When 회원 삭제 요청
     * Then 회원 삭제됨
     */
    @DisplayName("회원 정보를 관리한다.")
    @Test
    void manageMember() {
        // when
        ExtractableResponse<Response> createResponse = 회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        회원_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 회원_정보_조회_요청(createResponse);

        // then
        회원_정보_조회됨(findResponse, EMAIL, AGE);

        // given
        String newEmail = "new" + EMAIL;
        String newPassword = "new" + PASSWORD;

        // when
        ExtractableResponse<Response> editResponse = 회원_정보_수정_요청(createResponse, newEmail, newPassword, AGE);

        // then
        회원_정보_수정됨(editResponse, newEmail);

        // when
        ExtractableResponse<Response> deleteResponse = 회원_삭제_요청(createResponse);

        // then
        회원_정보_삭제됨(deleteResponse);
    }

    /**
     * When 회원 생성을 요청
     * Then 회원 생성됨
     * When 내 정보 조회 요청
     * Then 내 정보 조회됨
     * When 내 정보 수정 요청
     * Then 내 정보 수정됨
     * When 회원 삭제 요청
     * Then 회원 삭제됨
     */
    @DisplayName("나의 정보를 생성, 조회, 수정, 삭제한다.")
    @Test
    void manageMyInfo() {
        // when
        회원_생성_요청(EMAIL, PASSWORD, AGE);

        // then
        String accessToken = 로그인_되어_있음(EMAIL, PASSWORD);

        // when
        ExtractableResponse<Response> findResponse = 내_회원_정보_조회_요청(accessToken);

        // then
        내_회원_정보_조회됨(findResponse, EMAIL, AGE);

        // given
        String newEmail = "new" + EMAIL;
        String newPassword = "new" + PASSWORD;

        // when
        ExtractableResponse<Response> editResponse = 내_회원_정보_수정_요청(accessToken, newEmail, newPassword, AGE);

        // then
        내_회원_정보_수정됨(editResponse, newEmail);

        // when
        ExtractableResponse<Response> deleteResponse = 내_회원_정보_삭제_요청(accessToken);

        // then
        내_회원_정보_삭제됨(deleteResponse);
    }
}